# AndroidRealmDBSample
## What is Realm?

Realm is another type of database in Android. But, what is very important, Realm doesn’t use SQLite. If you use ORMLite or ActiveAndroid or any other similar library, your data is stored in SQLite database, because these libraries give us only an overlay on SQLite. With Realm there is no SQLite at all. Thanks to that Realm is very fast and you are even allowed to write the data in UI thread (mostly).

## Gradle
Add the following line to your dependencies.
```java
dependencies {
    compile 'io.realm:realm-android:0.87.4'
    //other dependencies
}
```

## Simple example

I’ve created very simple project to show how the database works and how to use it. Application consists of one Activity (container) and two Fragments. First Fragment allows us to add a book which is stored in database (MyEditionFragment) and the second one shows a list of all books from the database (MyListFragment). And that’s it.

### RealmObject
To use your object as a Realm object you have to simply extend RealmObject class.
```java
public class MyBook extends RealmObject {
    @Required
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }
    ```


Annotation @Required means that title cannot be null.

### CRUD operations
All of the CRUD operations are made with the use of Realm instance. How?

1. Get a Realm instance.
```java
    private Realm mRealm;
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getInstance(getContext());
    }
    ```


2. Do your operations on database. For example add or remove a book.
```java
    @OnClick(R.id.button_add)
    public void onAddClick() {
        mRealm.beginTransaction();
        MyBook book = mRealm.createObject(MyBook.class);
        book.setTitle(getTrimmedTitle());
        mRealm.commitTransaction();
    }

    @OnClick(R.id.button_remove)
    public void onRemoveClick() {
        mRealm.beginTransaction();
        RealmResults<MyBook> books = mRealm.where(MyBook.class).equalTo("title", getTrimmedTitle()).findAll();
        if(!books.isEmpty()) {
            for(int i = books.size() - 1; i >= 0; i--) {
                books.get(i).removeFromRealm();
            }
        }
        mRealm.commitTransaction();
    }
    private String getTrimmedTitle() {
        return mEditTitle.getText().toString().trim();
    }
    ```


Each operation on database which is not the read operation, has to start with Realm.beginTransaction() and to end with Realm.commitTransaction(). There are two arguments for that – our data will always be in consistent state and it also ensures the thread safety. If you forget to commit your changes, they will not be saved. You can also cancel the transaction by calling Realm.cancelTransaction().

3. According to Realm documentation it is important to close all of the Realm instances when we are done with them.
```java
    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
    ```
