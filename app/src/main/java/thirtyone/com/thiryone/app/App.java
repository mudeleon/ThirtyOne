package thirtyone.com.thiryone.app;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;



import io.realm.Realm;
import io.realm.RealmConfiguration;


public class App extends Application {

    private static App sInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

    }

    public synchronized static App getInstance() {
        return sInstance;
    }



}
