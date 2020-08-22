/*
 *  Copyright 2019 TheRedSpy15
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package theredspy15.ltecleanerfoss;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.widget.TextView;

import com.fxn.stash.Stash;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static theredspy15.ltecleanerfoss.WhitelistActivity.getWhiteList;

public class FileScanner {

    private File path;
    private Resources res;
    private MainActivity gui;
    private int filesRemoved = 0;
    private long kilobytesTotal = 0;
    private boolean delete = false;
    private boolean emptyDir = false;
    private boolean autoWhite = true;
    private boolean corpse = false;
    private static ArrayList<String> filters = new ArrayList<>();
    private static String[] protectedFileList = {
            "backup", "copy", "copies", "important", "do_not_edit"};

    FileScanner(File path) {
        this.path = path;
    }
    private List<File> getListFiles() {
        return getListFiles(path);
    }

    /**
     * Used to generate a list of all files on device
     * @param parentDirectory where to start searching from
     * @return List of all files on device (besides whitelisted ones)
     */
    private synchronized List<File> getListFiles(File parentDirectory) {
        ArrayList<File> inFiles = new ArrayList<>();
        File[] files = parentDirectory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (!isWhiteListed(file)) { // won't touch if whitelisted
                    if (file.isDirectory()) { // folder

                        if (autoWhite) {
                            if (!autoWhiteList(file))
                                inFiles.add(file);
                        }
                        else inFiles.add(file); // add folder itself

                        inFiles.addAll(getListFiles(file)); // add contents to returned list

                    } else inFiles.add(file); // add file
                }
            }
        }

        return inFiles;
    }

    /**
     * Runs a for each loop through the white list, and compares the path of the file
     * to each path in the list
     * @param file file to check if in the whitelist
     * @return true if is the file is in the white list, false if not
     */
    private synchronized boolean isWhiteListed(File file) {
        for (String path : getWhiteList())
            if (path.equalsIgnoreCase(file.getAbsolutePath())
                    || path.equalsIgnoreCase(file.getName()))
                return true;
        return false;
    }

    /**
     * Runs before anything is filtered/cleaned. Automatically adds folders to the whitelist
     * based on the name of the folder itself
     * @param file file to check whether it should be added to the whitelist
     */
    private synchronized boolean autoWhiteList(File file) {

        for (String protectedFile : protectedFileList) {
            if (file.getName().toLowerCase().contains(protectedFile) &&
                    !getWhiteList().contains(file.getAbsolutePath().toLowerCase())) {
                getWhiteList().add(file.getAbsolutePath().toLowerCase());
                Stash.put("whiteList", getWhiteList());
                return true;
            }
        }

        return false;
    }

    /**
     * Runs as for each loop through the filter, and checks if
     * the file matches any filters
     * @param file file to check
     * @return true if the file's extension is in the filter, false otherwise
     */
    public synchronized boolean filter(File file) {
        // corpse checking - TODO: needs improved!
        if (file.getParentFile() != null && file.getParentFile().getParentFile() != null&& corpse)
            if (file.getParentFile().getName().equals("data") && file.getParentFile().getParentFile().getName().equals("Android"))
                if (!getInstalledPackages().contains(file.getName()) && !file.getName().equals(".nomedia"))
                    return true;

        if (file.isDirectory() && isDirectoryEmpty(file)
                && emptyDir) return true; // empty folder

        for (String filter : filters)
            if (file.getAbsolutePath().toLowerCase()
                    .matches(filter.toLowerCase()))
                return true; // file

        return false; // not empty folder or file in filter
    }

    private synchronized List<String> getInstalledPackages() {
        final PackageManager pm = gui.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        List<String> packagesString = new LinkedList<>();
        for (ApplicationInfo packageInfo : packages) {
            packagesString.add(packageInfo.packageName);
        }
        return packagesString;
    }

    /**
     * lists the contents of the file to an array, if the array length is 0, then return true,
     * else false
     * @param directory directory to test
     * @return true if empty, false if containing a file(s)
     */
    private synchronized boolean isDirectoryEmpty(File directory) {

        return Objects.requireNonNull(directory.listFiles()).length == 0;
    }

    /**
     * Adds paths to the white list that are not to be cleaned. As well as adds
     * extensions to filter. 'generic', 'aggressive', and 'apk' should be assigned
     * by calling preferences.getBoolean()
     */
    @SuppressLint("ResourceType")
    synchronized void setUpFilters(boolean generic, boolean aggressive, boolean apk) {
        List<String> folders = new ArrayList<>();
        List<String> files = new ArrayList<>();

        if (gui != null)
            setResources(gui.getResources());

        if (generic) {
            folders.addAll(Arrays.asList(res.getStringArray(R.array.generic_filter_folders)));
            files.addAll(Arrays.asList(res.getStringArray(R.array.generic_filter_files)));
        }

        if (aggressive) {
            folders.addAll(Arrays.asList(res.getStringArray(R.array.aggressive_filter_folders)));
            files.addAll(Arrays.asList(res.getStringArray(R.array.aggressive_filter_files)));
        }

        // filters
        filters.clear();
        for (String folder : folders)
            filters.add(getRegexForFolder(folder));
        for (String file : files)
            filters.add(getRegexForFile(file));

        // apk
        if (apk) filters.add(getRegexForFile(".apk"));
    }

    long startScan() {
        byte cycles = 0;
        byte maxCycles = 10;
        List<File> foundFiles;
        if (!delete) maxCycles = 1; // when nothing is being deleted. Stops duplicates from being found

        // removes the need to 'clean' multiple times to get everything
        while (cycles < maxCycles) {

            // find files
            foundFiles = getListFiles();
            if (gui != null) gui.scanPBar.setMax(gui.scanPBar.getMax() + foundFiles.size());

            // scan & delete
            for (File file : foundFiles) {
                if (filter(file)) { // filter
                    TextView tv = null;
                    if (gui != null)
                        tv = gui.displayPath(file);

                    if (delete) {
                        kilobytesTotal += file.length();
                        ++filesRemoved;
                        if (file.delete()) { // deletion

                        } else if (tv != null) tv.setTextColor(Color.GRAY); // error effect
                    } else {
                        kilobytesTotal += file.length();
                    }
                }

                if (gui != null) {
                    // progress
                    gui.runOnUiThread(() -> gui.scanPBar.setProgress(gui.scanPBar.getProgress() + 1));
                    double scanPercent = gui.scanPBar.getProgress() * 100.0 / gui.scanPBar.getMax();
                    gui.runOnUiThread(() -> gui.progressText.setText(String.format(Locale.US, "%.0f", scanPercent) + "%"));
                }
            }

            if (filesRemoved == 0) break; // nothing found this run, no need to run again

            filesRemoved = 0; // reset for next cycle
            ++cycles;
        }

        return kilobytesTotal;
    }

    private String getRegexForFolder(String folder) {
        return ".*(\\\\|/)" + folder + "(\\\\|/|$).*";
    }

    private String getRegexForFile(String file) {
        return ".+"+ file.replace(".", "\\.") + "$";
    }

    void setGUI(MainActivity gui) {
        this.gui = gui;
    }

    void setResources(Resources res) {
        this.res = res;
    }

    void setEmptyDir(boolean emptyDir) {
        this.emptyDir = emptyDir;
    }

    void setDelete(boolean delete) {
        this.delete = delete;
    }

    void setCorpse(boolean corpse) {
        this.corpse = corpse;
    }

    void setAutoWhite(boolean autoWhite) {
        this.autoWhite = autoWhite;
    }
}
