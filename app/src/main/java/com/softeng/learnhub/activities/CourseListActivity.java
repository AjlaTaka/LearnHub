package com.softeng.learnhub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.softeng.learnhub.R;
import com.softeng.learnhub.adapters.CourseAdapter;
import com.softeng.learnhub.models.Course;
import com.softeng.learnhub.utils.DataRepository;
import com.softeng.learnhub.utils.PreferencesManager;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.List;

/**
 * CourseListActivity — displays all 5 Software Engineering courses in a RecyclerView.
 *
 * Reached via explicit Intent from MainActivity.
 * Navigates to CourseDetailActivity via explicit Intent when a course is tapped.
 *
 * Architecture role (MVC): Controller — retrieves data from DataRepository (Model)
 * and binds it to CourseAdapter (View).
 */
public class CourseListActivity extends AppCompatActivity implements CourseAdapter.OnCourseClickListener {

    public static final String EXTRA_COURSE_ID = "extra_course_id";

    private RecyclerView recyclerView;
    private CourseAdapter adapter;
    private PreferencesManager prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = PreferencesManager.getInstance(this);

        if (prefs.isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        prefs = PreferencesManager.getInstance(this);

        setupToolbar();
        setupRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh to show updated completion badges
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    // ── Toolbar ───────────────────────────────────────────────────────────────

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Courses");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Back button: respect back stack
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ── RecyclerView ──────────────────────────────────────────────────────────

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recycler_courses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Course> courses = DataRepository.getInstance().getAllCourses();
        adapter = new CourseAdapter(courses, prefs, this);
        recyclerView.setAdapter(adapter);
    }

    // ── CourseAdapter.OnCourseClickListener ───────────────────────────────────

    @Override
    public void onCourseClick(Course course) {
        // Explicit Intent passing course ID to CourseDetailActivity
        Intent intent = new Intent(this, CourseDetailActivity.class);
        intent.putExtra(EXTRA_COURSE_ID, course.getId());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
