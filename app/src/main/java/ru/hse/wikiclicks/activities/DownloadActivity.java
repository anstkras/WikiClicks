package ru.hse.wikiclicks.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.controllers.ChooseOfflineGame;
import ru.hse.wikiclicks.controllers.OfflineController;

public class DownloadActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        String directory = getFilesDir().getAbsolutePath() + "/wiki/";
        OfflineController.setOutputDirectory(directory);

        ChooseOfflineGame firstGame = new ChooseOfflineGame("Demonophobia", "Qalb", 2);
        OfflineController.downloadTree(firstGame);

        ChooseOfflineGame secondGame = new ChooseOfflineGame("Sexuality of Adolf Hitler", "Squatting", 2);
        OfflineController.downloadTree(secondGame);

        ChooseOfflineGame thirdGame = new ChooseOfflineGame("Vanity Fair (novel)", "Star Wars", 2);
        OfflineController.downloadTree(thirdGame);

        TextView text = findViewById(R.id.download_text);
        text.setText("Download finished!");
    }
}
