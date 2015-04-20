package ro.minimul.mg;

import android.content.SharedPreferences;

public interface GameSettings {
    public void maybeCommit(SharedPreferences prefs);
    public void get(SharedPreferences prefs);
}
