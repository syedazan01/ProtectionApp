package atoz.protection.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import atoz.protection.utils.RecordingException;
import atoz.protection.utils.RecordingThreadAac;
import atoz.protection.utils.RecordingThreadWav;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.media.MediaRecorder.AudioSource.MIC;
import static android.media.MediaRecorder.AudioSource.VOICE_CALL;
import static android.media.MediaRecorder.AudioSource.VOICE_COMMUNICATION;
import static android.media.MediaRecorder.AudioSource.VOICE_RECOGNITION;


@SuppressWarnings("CatchMayIgnoreException")
public class Recorder {
    public static final String WAV_FORMAT = "wav";
    public static final String AAC_HIGH_FORMAT = "aac_hi";
    public static final String AAC_MEDIUM_FORMAT = "aac_med";
    public static final String AAC_BASIC_FORMAT = "aac_bas";
    public static final String MONO = "mono";
    private static final String ACRA_FORMAT = "format";
    private static final String ACRA_MODE = "mode";
    private static final String ACRA_SAVE_PATH = "save_path";
    private final String format;
    private final String mode;
    private File audioFile;
    private Thread recordingThread;
    private long startingTime;
    private SharedPreferences settings;
    private int source;
    private boolean hasError = false;
    private Context context;

    public Recorder(Context context) {
        this.context = context;
        settings = PreferenceManager.getDefaultSharedPreferences(context);
        format = AAC_HIGH_FORMAT;
        mode = MONO;
    }

    long getStartingTime() {
        return startingTime;
    }

    String getAudioFilePath() {
        return audioFile.getAbsolutePath();
    }

    public void startRecording(String phoneNumber) throws RecordingException {
        if (phoneNumber == null)
            phoneNumber = "";

        if (isRunning())
            stopRecording();
        String extension = format.equals(WAV_FORMAT) ? ".wav" : ".aac";
        File recordingsDir;

        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SoundRecorder/";
        recordingsDir = (filePath == null) ? context.getExternalFilesDir(null) : new File(filePath);
        if (recordingsDir == null) //recordingsDir poate fi null în cazul în care getExternalFilesDir(null) returnează null, adică nu e montat (disponibil) un astfel de spațiu.
            recordingsDir = context.getFilesDir();

//            phoneNumber = phoneNumber.replaceAll("[()/.,* ;+]", "_");
        String fileName = "Recording" + new SimpleDateFormat("-d-MMM-yyyy-HH-mm-ss", Locale.US).
                format(new Date(System.currentTimeMillis())) + extension;
        audioFile = new File(recordingsDir, fileName);
        Log.e("CrLog.DEBUG", String.format("Recording session started. Format: %s. Mode: %s. Save path: %s",
                format, mode, audioFile.getAbsolutePath()));
        //This data is cleared in RecorderService::onDestroy().
        try {
//                ACRA.getErrorReporter().putCustomData(ACRA_FORMAT, format);
//                ACRA.getErrorReporter().putCustomData(ACRA_MODE, mode);
//                ACRA.getErrorReporter().putCustomData(ACRA_SAVE_PATH, audioFile.getAbsolutePath());
        } catch (IllegalStateException exc) {
        }

        if (format.equals(WAV_FORMAT))
            recordingThread = new Thread(new RecordingThreadWav(context, mode, this));
        else
            recordingThread = new Thread(new RecordingThreadAac(context, audioFile, format, mode, this));

        recordingThread.start();
        startingTime = System.currentTimeMillis();
    }

    public void stopRecording() {
        if (recordingThread != null) {
            Log.e("CrLog.DEBUG", "Recording session ended.");
            recordingThread.interrupt();
            recordingThread = null;
            if (format.equals(WAV_FORMAT)) {
                //în cazul în care a apărut o eroare în RecordingThreadWav și fișierul temporar nu există, această
                //condiție va fi detectată în bucla try a CopyPcmToWav.run() și va fi raportată o eroare.
                Thread copyPcmToWav = new Thread(new RecordingThreadWav.CopyPcmToWav(context, audioFile, mode, this));
                copyPcmToWav.start();
            }
        }
    }

    public boolean isRunning() {
        return recordingThread != null && recordingThread.isAlive();
    }

    public String getFormat() {
        return format;
    }

    public String getMode() {
        return mode;
    }

    public String getSource() {
        switch (source) {
            case VOICE_RECOGNITION:
                return "Voice recognition";
            case VOICE_COMMUNICATION:
                return "Voice communication";
            case VOICE_CALL:
                return "Voice call";
            case MIC:
                return "Microphone";
            default:
                return "Source unrecognized";
        }
    }

    public void setSource(int source) {
        this.source = source;
    }

    public boolean hasError() {
        return hasError;
    }

    public void setHasError() {
        this.hasError = true;
    }
}
