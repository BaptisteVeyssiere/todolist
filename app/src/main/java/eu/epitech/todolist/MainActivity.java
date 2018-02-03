package eu.epitech.todolist;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    public final static int ADD_REQUEST = 1;
    public final static int EDIT_REQUEST = 2;
    public final static String DATE_CONVERT = "strftime('%d %m %Y at %H:%M', date)";
    public final static String BASIC_REQUEST = "SELECT id AS _id, title, description, " + DATE_CONVERT + " AS cdate, commentary, status FROM Task";
    public final static String ALPHASORT_REQUEST = " ORDER BY title ASC";
    public final static String RALPHASORT_REQUEST = " ORDER BY title DESC";
    public final static String STATUSSORT_REQUEST = " ORDER BY status ASC";
    public final static String DATESORT_REQUEST = " ORDER BY date ASC";
    private String request = BASIC_REQUEST;
    private ListView list = null;
    private ImageButton deleteButton = null;
    private Toolbar toolbar = null;
    private SearchView searchView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        display();
    }

    private void setListeners() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parentView, View childView, int position, long id)
            {
                if (deleteButton != null) {
                    deleteButton.setVisibility(View.GONE);
                    deleteButton = null;
                }
                Cursor tmp = (Cursor) parentView.getItemAtPosition(position);
                Intent editor = new Intent(MainActivity.this, EditorActivity.class);
                editor.putExtra("TASK", new Task(tmp.getLong(0), tmp.getString(1),
                        tmp.getString(2), tmp.getString(3), tmp.getString(4),
                        tmp.getString(5)));
                startActivityForResult(editor, EDIT_REQUEST);
            }
        });
        list.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            @Override
            public void onSwipeRight() {
                if (deleteButton != null) {
                    deleteButton.setVisibility(View.GONE);
                }
                deleteButton = currentView.findViewById(R.id.delete);
                deleteButton.setVisibility(View.VISIBLE);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor cursor = (Cursor) list.getItemAtPosition(currentPosition);
                        TaskDAO database = new TaskDAO(MainActivity.this);
                        database.open();
                        database.delete(cursor.getLong(0));
                        database.close();
                        display();
                    }
                });
            }
            @Override
            public void onSwipeLeft() {
                deleteButton.setVisibility(View.GONE);
                deleteButton = null;
            }
        });
        FloatingActionButton fab = findViewById(R.id.addbutton);
        fab.setBackgroundTintList(ColorStateList.valueOf(0xF5F5F5));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteButton != null) {
                    deleteButton.setVisibility(View.GONE);
                    deleteButton = null;
                }
                Intent creator = new Intent(MainActivity.this, CreatorActivity.class);
                startActivityForResult(creator, ADD_REQUEST);
            }
        });
    }

    protected void display() {
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        deleteButton = null;
        list = findViewById(R.id.listview);
        TaskDAO database = new TaskDAO(this);
        database.open();

        Cursor cursor = database.select(request, null);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.task_list, cursor,
                new String[]{TaskDAO.TITLE, TaskDAO.DESCRIPTION, "c" + TaskDAO.DATE, TaskDAO.COMMENTARY, TaskDAO.STATUS},
                new int[]{R.id.title, R.id.description, R.id.date, R.id.commentary, R.id.status});
        list.setAdapter(adapter);
        setListeners();

        database.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_REQUEST && resultCode == RESULT_OK) {
            TaskDAO database = new TaskDAO(this);
            database.open();
            Task elem = data.getParcelableExtra("TASK");
            database.add(elem);
            database.close();
            request = BASIC_REQUEST;
            display();
        } else if (requestCode == EDIT_REQUEST && resultCode == RESULT_OK) {
            TaskDAO database = new TaskDAO(this);
            database.open();
            Task elem = data.getParcelableExtra("TASK");
            database.edit(elem);
            database.close();
            display();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem myActionMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                request = BASIC_REQUEST + " WHERE title LIKE \"%" + query + "%\"";
                display();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.alphasort || item.getItemId() == R.id.ralphasort ||
                item.getItemId() == R.id.statussort || item.getItemId() == R.id.datesort ||
                item.getItemId() == R.id.defaultsort) {
            if (request.contains(ALPHASORT_REQUEST)) {
                request = request.replace(ALPHASORT_REQUEST, "");
            } else if (request.contains(RALPHASORT_REQUEST)) {
                request = request.replace(RALPHASORT_REQUEST, "");
            } else if (request.contains(STATUSSORT_REQUEST)) {
                request = request.replace(STATUSSORT_REQUEST, "");
            } else if (request.contains(DATESORT_REQUEST)) {
            request = request.replace(DATESORT_REQUEST, "");
            }
            switch (item.getItemId()) {
                case R.id.alphasort:
                    request += ALPHASORT_REQUEST;
                    display();
                    return true;
                case R.id.ralphasort:
                    request += RALPHASORT_REQUEST;
                    display();
                    return true;
                case R.id.statussort:
                    request += STATUSSORT_REQUEST;
                    display();
                    return true;
                case R.id.datesort:
                    request += DATESORT_REQUEST;
                    display();
                    return true;
                default:
                    display();
                    return true;
            }
        } else if (item.getItemId() == R.id.reset) {
            request = BASIC_REQUEST;
            display();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
