package ru.hse.wikiclicks.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.controllers.wiki.WikiController;
import ru.hse.wikiclicks.controllers.wiki.WikiPage;
import ru.hse.wikiclicks.controllers.modes.LevelGameMode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/** Activity for choosing level of challenges and viewing leader boards */
public class ChallengesActivity extends AppCompatActivity {
    private final static int LEVEL0 = 0;
    private final static int LEVEL1 = 1;
    private final static int LEVEL2 = 2;
    private final static int LEVEL3 = 3;
    private final static int LEVEL4 = 4;
    private final static int LEVEL5 = 5;
    private final static int LEVEL6 = 6;
    private final static int LEVEL7 = 7;
    private final static int LEVEL8 = 8;
    private final static int LEVEL9 = 9;

    private final static String[] startPages = {"Dog", "Women in Russia", "Algorithm",
            "Saint Petersburg Academic University", "Terry Pratchett", "War and Peace", "Devil",
            "Black metal",  "Anne Hathaway (wife of Shakespeare)", "Empty"};
    private final static String[] endPages = {"Cat", "Google", "Javascript",
            "Higher School of Economics", "Hamiltonian path", "Hamlet", "Invisible Pink Unicorn",
            "Wombat",  "Anne Hathaway", "Full"};

    private Button signInButton;
    private Button signOutButton;
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        final GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        setUpButtonForLevel((Button) findViewById(R.id.level0), LEVEL0);
        setUpButtonForLevel((Button) findViewById(R.id.level1), LEVEL1);
        setUpButtonForLevel((Button) findViewById(R.id.level2), LEVEL2);
        setUpButtonForLevel((Button) findViewById(R.id.level3), LEVEL3);
        setUpButtonForLevel((Button) findViewById(R.id.level4), LEVEL4);
        setUpButtonForLevel((Button) findViewById(R.id.level5), LEVEL5);
        setUpButtonForLevel((Button) findViewById(R.id.level6), LEVEL6);
        setUpButtonForLevel((Button) findViewById(R.id.level7), LEVEL7);
        setUpButtonForLevel((Button) findViewById(R.id.level8), LEVEL8);
        setUpButtonForLevel((Button) findViewById(R.id.level9), LEVEL9);

        setUpButtonForLeaderBoard((Button) findViewById(R.id.leaderboard_level0_button), getString(R.string.leaderboard_level_0));
        setUpButtonForLeaderBoard((Button) findViewById(R.id.leaderboard_level1_button), getString(R.string.leaderboard_level_1));
        setUpButtonForLeaderBoard((Button) findViewById(R.id.leaderboard_level2_button), getString(R.string.leaderboard_level_2));
        setUpButtonForLeaderBoard((Button) findViewById(R.id.leaderboard_level3_button), getString(R.string.leaderboard_level_3));
        setUpButtonForLeaderBoard((Button) findViewById(R.id.leaderboard_level4_button), getString(R.string.leaderboard_level_4));
        setUpButtonForLeaderBoard((Button) findViewById(R.id.leaderboard_level5_button), getString(R.string.leaderboard_level_5));
        setUpButtonForLeaderBoard((Button) findViewById(R.id.leaderboard_level6_button), getString(R.string.leaderboard_level_6));
        setUpButtonForLeaderBoard((Button) findViewById(R.id.leaderboard_level7_button), getString(R.string.leaderboard_level_7));
        setUpButtonForLeaderBoard((Button) findViewById(R.id.leaderboard_level8_button), getString(R.string.leaderboard_level_8));
        setUpButtonForLeaderBoard((Button) findViewById(R.id.leaderboard_level9_button), getString(R.string.leaderboard_level_9));

        setUpSignInButton();
        setUpSignOutButton();
    }

    private void setUpButtonForLevel(Button button, final int level) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(level);
            }
        });
    }

    private void setUpButtonForLeaderBoard(Button button, final String leaderBoardId) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeaderboard(leaderBoardId);
            }
        });
    }

    private void setUpSignInButton() {
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void signOut() {
        googleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        signInButton.setVisibility(View.VISIBLE);
                        signOutButton.setVisibility(View.GONE);
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast toast = Toast.makeText(this, "sing in successful", Toast.LENGTH_LONG);
            toast.show();
            signInButton.setVisibility(View.GONE);
            signOutButton.setVisibility(View.VISIBLE);
        } catch (ApiException e) {
            Toast toast = Toast.makeText(this, "Something went wrong. Error code: " + e.getStatusCode(), Toast.LENGTH_LONG);
            toast.show();
            signInButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            signInButton.setVisibility(View.GONE);
            signOutButton.setVisibility(View.VISIBLE);
        }
    }

    private void setUpSignOutButton() {
        signOutButton = findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        signOutButton.setVisibility(View.GONE);
    }

    private void startGame(int level) {
        WikiPage startPage = WikiController.getPageFromUrl(WikiController.getUrlForTitle(startPages[level]));
        WikiPage finishPage = WikiController.getPageFromUrl(WikiController.getUrlForTitle(endPages[level]));

        Intent startGame = new Intent(ChallengesActivity.this, GameActivity.class);
        Bundle pagesInfo = new Bundle();
        pagesInfo.putString(GetEndpointsActivity.START_TITLE_KEY, startPage.getTitle());
        pagesInfo.putString(GetEndpointsActivity.FINISH_ID_KEY, finishPage.getId());
        pagesInfo.putString(GetEndpointsActivity.FINISH_TITLE_KEY, finishPage.getTitle());
        pagesInfo.putParcelable(SelectModeActivity.GAME_MODE_KEY, new LevelGameMode(level));
        startGame.putExtras(pagesInfo);
        startActivity(startGame);
    }

    private static final int RC_LEADERBOARD_UI = 9004;

    private void showLeaderboard(String leaderBoardId) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            Toast toast = Toast.makeText(this, "You have to sign in", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        Games.getLeaderboardsClient(this, account)
                .getLeaderboardIntent(leaderBoardId)
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_LEADERBOARD_UI);
                    }
                });
    }
}
