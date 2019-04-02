package ru.hse.wikiclicks.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.List;

import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.controllers.WikiController;
import ru.hse.wikiclicks.controllers.WikiPage;

public class GetStartActivity extends AppCompatActivity {
    private final WikiPage startPage = new WikiPage();
    private final WikiPage finishPage = new WikiPage();
    static final String FINISH_TITLE_KEY = "finish_title";
    static final String FINISH_ID_KEY = "finish_id";
    static final String START_ID_KEY = "start_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpoint);
        final SearchView startPoint = findViewById(R.id.choose_start_point);
        final SearchView finishPoint = findViewById(R.id.choose_finish_point);
        createSearch(startPoint, startPage);
        createSearch(finishPoint, finishPage);

        Button randomButton = findViewById(R.id.random_button);
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPage.set(WikiController.getRandomPage());
                startPoint.setQuery(startPage.getTitle(), true);
                finishPage.set(WikiController.getRandomPage());
                finishPoint.setQuery(finishPage.getTitle(), true);
            }
        });

        Button startButton = findViewById(R.id.ok_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startPage.getId() == null) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please choose starting page.", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (finishPage.getId() == null) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please choose end page.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Intent startGame = new Intent(GetStartActivity.this, GameActivity.class);
                    Bundle pagesInfo = new Bundle();
                    pagesInfo.putString(START_ID_KEY, startPage.getId());
                    pagesInfo.putString(FINISH_ID_KEY, finishPage.getId());
                    pagesInfo.putString(FINISH_TITLE_KEY, finishPage.getTitle());
                    startGame.putExtras(pagesInfo);
                    startActivity(startGame);
                }
            }
        });
    }

    private void createSearch(final SearchView pageSearch, final WikiPage chosenPage) {
        pageSearch.setIconifiedByDefault(false);
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.suggestion_layout, null,
                new String[]{"title"}, new int[]{R.id.title}, 0);
        pageSearch.setSuggestionsAdapter(adapter);
        pageSearch.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                System.out.println(position);
                Cursor suggestions = pageSearch.getSuggestionsAdapter().getCursor();
                String title = suggestions.getString(1);
                String pageId = suggestions.getString(2);
                chosenPage.set(new WikiPage(title, pageId));
                pageSearch.setQuery(suggestions.getString(1), true);
                return true;
            }
        });
        pageSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals(chosenPage.getTitle())) { // correct suggestion
                    adapter.swapCursor(new MatrixCursor(new String[]{BaseColumns._ID, "title", "id"}));
                    return true;
                }
                chosenPage.clear();
                List<WikiPage> suggestions = WikiController.getSearchSuggestions(newText);
                MatrixCursor cursor = new MatrixCursor(new String[]{BaseColumns._ID, "title", "id"});
                int id = 0;
                for (WikiPage page : suggestions) {
                    cursor.newRow().add(id++).add(page.getTitle()).add(page.getId());
                }
                adapter.swapCursor(cursor);
                return true;
            }
        });
    }
}
