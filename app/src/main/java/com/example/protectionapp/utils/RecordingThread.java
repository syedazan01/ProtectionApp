package com.example.protectionapp.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.preference.PreferenceManager;

import com.example.protectionapp.R;
import com.example.protectionapp.model.Recorder;
import com.example.protectionapp.services.CallRecorderService;

import static android.media.MediaRecorder.AudioSource.VOICE_RECOGNITION;

abstract class RecordingThread {
    static final int SAMPLE_RATE = 44100;
    protected final Recorder recorder;
    final int channels;
    final int bufferSize;
    final AudioRecord audioRecord;
    protected Context context;

    RecordingThread(Context context, String mode, Recorder recorder) throws RecordingException {
        this.context = context;
        channels = (mode.equals(Recorder.MONO) ? 1 : 2);
        this.recorder = recorder;
        bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, channels == 1 ? AudioFormat.CHANNEL_IN_MONO : AudioFormat.CHANNEL_IN_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);
        audioRecord = createAudioRecord();
        audioRecord.startRecording();
    }

    //e statică ca să poată fi apelată din CopyPcmToWav
    static void notifyOnError(Context context) {
        CallRecorderService service = CallRecorderService.getService();
        if (service != null) {
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (nm != null)
                nm.notify(CallRecorderService.NOTIFICATION_ID,
                        service.buildNotification(CallRecorderService.RECORD_ERROR,
                                R.string.notify_error));
        }
    }

    private AudioRecord createAudioRecord() throws RecordingException {
        AudioRecord audioRecord;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        int source = Integer.valueOf(String.valueOf(VOICE_RECOGNITION));
        try {
            audioRecord = new AudioRecord(source, SAMPLE_RATE,
                    channels == 1 ? AudioFormat.CHANNEL_IN_MONO : AudioFormat.CHANNEL_IN_STEREO,
                    AudioFormat.ENCODING_PCM_16BIT, bufferSize * 10);
        } catch (Exception e) { //La VOICE_CALL dă IllegalArgumentException. Aplicația nu se oprește, rămîne
            //hanging, nu înregistrează nimic.
            throw new RecordingException(e.getMessage());
        }

        if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
            CrLog.log(CrLog.DEBUG, "createAudioRecord(): Audio source chosen: " + source);
            recorder.setSource(audioRecord.getAudioSource());
        }

        if (audioRecord.getState() != AudioRecord.STATE_INITIALIZED)
            throw new RecordingException("Unable to initialize AudioRecord");

        return audioRecord;
    }

    void disposeAudioRecord() {
        audioRecord.stop();
        audioRecord.release();
    }
}
