package ru.hse.wikiclicks.activities;

import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

import java.util.List;

import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.controllers.MainController;
import ru.hse.wikiclicks.controllers.WikiPage;

public class GetStartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpoint);
        createSearch();
        createRandomButton();
    }

    private void createRandomButton() {
        Button randomButton = findViewById(R.id.RandomButton);
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startGame = new Intent(GetStartActivity.this, MainActivity.class);
                startActivity(startGame);
            }
        });
    }

    private void createSearch() {
        final SearchView startPoint = findViewById(R.id.ChooseStartPoint);
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.suggestion_layout, null,
                new String[]{"title"}, new int[]{R.id.title}, 0);
        startPoint.setSuggestionsAdapter(adapter);
        startPoint.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                System.out.println(position);
                String pageId = startPoint.getSuggestionsAdapter().getCursor().getString(2);
                Intent chooseStart = new Intent(GetStartActivity.this, MainActivity.class);
                Bundle startPageId = new Bundle();
                startPageId.putString("id", pageId);
                chooseStart.putExtras(startPageId);
                startActivity(chooseStart);
                return true;
            }
        });
        startPoint.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<WikiPage> suggestions = MainController.getSearchSuggestions(newText);
                MatrixCursor cursor = new MatrixCursor(new String[] {BaseColumns._ID, "title", "id"});
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
