# Customization 
## UpdateChecker object methods

This is the base code.
```java
UpdateChecker checker = new UpdateChecker(this);
checker.start();
```
You can declare the store where your app is published on, the successful checks necessary to show notice, modify the notice(Dialog or Notification) and modify the notice icon.

###setStore(Store store)

```java
UpdateChecker checker = new UpdateChecker(this);
checker.setStore(Store.GOOGLE_PLAY)
checker.start();
```
Declare the store where your are is published on. If you publish in more than one store modify this value before exporting the .apk for every store.
Stores supported:

* GOOGLE_PLAY = Google Play Store 
* AMAZON = Amazon App Store

More stores coming soon...

###setSuccessfulChecksRequired(int checksRequired)

```java
UpdateChecker checker = new UpdateChecker(this);
checker.setSuccessfulChecksRequired(1);
checker.start();
```
Set the checks successful necessary to show the Notice. [Default is 5](https://github.com/rampo/UpdateChecker/blob/masterd/library/src/main/java/com/rampo/updatechecker/UpdateChecker.java#L36)

###setNotice(Notice notice)

```java
UpdateChecker checker = new UpdateChecker(this);
checker.setNotice(Notice.NOTIFICATION);
checker.start();
```
Set the notice. Notices supported:

* NOTIFICATION = Show a Notification
* DIALOG = Show a Dialog

See [Custom implementation](https://github.com/rampo/UpdateChecker/blob/master/CHANGELOG.md#custom-implementation) if you don't want to use these Notices and manage the result by yourself.
###setNoticeIcon(int noticeIconResId)

```java
UpdateChecker checker = new UpdateChecker(this);
checker.setNoticeIcon(R.drawable.ic_launcher);
checker.start();
```
Set the Notification or Dialog icon.

## Custom implementation

You can add a custom implementation in your activity by implementing `UpdateCheckerResult`. You will be able to manage the String versionDownloadable and show a notice by yourself.

You need to use the constructor with two argoments and implement UpdateCheckerResult from the class...

  ```java
  public class CustomActivity extends Activity implements UpdateCheckerResult {
  ...
  UpdateChecker checker = new UpdateChecker(this, this);
  ```
  
...or create a new instance
  ```java
     UpdateChecker checker = new UpdateChecker(this, new UpdateCheckerResult() {
            @Override
            public void foundUpdateAndShowIt(String s) {
                
            }

            @Override
            public void foundUpdateAndDontShowIt(String s) {

            }

            @Override
            public void upToDate(String s) {

            }
        });
  ```

Example: (See [CustomActivity.java](https://github.com/rampo/UpdateChecker/blob/master/demo/src/main/java/com/rampo/updatechecker/demo/CustomActivity.java) for more)
```java
public class CustomActivity extends Activity implements UpdateCheckerResult {
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_activity);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setSubtitle(R.string.custom);
        result = (TextView) findViewById(R.id.result);
    }
    
    public void custom_impl(View view) {
        UpdateChecker checker = new UpdateChecker(this, this);
        checker.setSuccessfulChecksRequired(2);
        checker.start();
        result.setText(R.string.loading);
    }

    @Override
    public void foundUpdateAndShowIt(String mVersionDonwloadable) {
        result.setText("Update available\n" + "Version downloadable: " + mVersionDonwloadable + "\nVersion installed: " + mVersionInstalled());
    }

    @Override
    public void foundUpdateAndDontShowIt(String mVersionDonwloadable) {
        result.setText("Already Shown\n" + "Version downloadable: " + mVersionDonwloadable + "\nVersion installed: " + mVersionInstalled());
    }

    @Override
    public void upToDate(String mVersionDonwloadable) {
        result.setText("Updated\n" + "Version downloadable: " + mVersionDonwloadable + "\nVersion installed: " + mVersionInstalled());
    }

    public String mVersionInstalled() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return null;
    }
}
```
