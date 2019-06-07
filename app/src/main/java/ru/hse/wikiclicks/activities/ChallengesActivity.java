package ru.hse.wikiclicks.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.controllers.WikiController;
import ru.hse.wikiclicks.controllers.WikiPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

public class ChallengesActivity extends AppCompatActivity {
    public final static int LEVEL0 = 0;
    public final static int LEVEL1 = 1;
    public final static int LEVEL2 = 2;
    public final static int LEVEL3 = 3;
    public final static int LEVEL4 = 4;
    public final static int LEVEL5 = 5;
    public final static int LEVEL6 = 6;
    public final static int LEVEL7 = 7;
    public final static int LEVEL8 = 8;
    public final static int LEVEL9 = 9;

    private final static String[] startPages = {"Dog", "Women in Russia", "Algorithm",
            "Saint Petersburg Academic University", "Terry Pratchett", "War and Peace", "Devil",
            "Black metal",  "Anne Hathaway (wife of Shakespeare)", "Empty"};
    private final static String[] endPages = {"Cat", "Google", "Javascript",
            "Higher School of Economics", "Hamiltonian path", "Hamlet", "Invisible Pink Unicorn",
            "Wombat",  "Anne Hathaway", "Full"};

    public final static String LEVEL_KEY = "level";
    private Button signInButton;
    private Button signOutButton;
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .build();
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

        Button level0LeaderBoardButton = findViewById(R.id.leaderboard_level0_button);
        level0LeaderBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeaderboard(getString(R.string.leaderboard_level_0));
            }
        });
        Button level1LeaderBoardButton = findViewById(R.id.leaderboard_level1_button);
        level1LeaderBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeaderboard(getString(R.string.leaderboard_level_1));
            }
        });
        Button level2LeaderBoardButton = findViewById(R.id.leaderboard_level2_button);
        level2LeaderBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeaderboard(getString(R.string.leaderboard_level_2));
            }
        });
        Button level3LeaderBoardButton = findViewById(R.id.leaderboard_level3_button);
        level3LeaderBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeaderboard(getString(R.string.leaderboard_level_3));
            }
        });
        Button level4LeaderBoardButton = findViewById(R.id.leaderboard_level4_button);
        level4LeaderBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeaderboard(getString(R.string.leaderboard_level_4));
            }
        });
        Button level5LeaderBoardButton = findViewById(R.id.leaderboard_level5_button);
        level5LeaderBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeaderboard(getString(R.string.leaderboard_level_5));
            }
        });
        Button level6LeaderBoardButton = findViewById(R.id.leaderboard_level6_button);
        level6LeaderBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeaderboard(getString(R.string.leaderboard_level_6));
            }
        });
        Button level7LeaderBoardButton = findViewById(R.id.leaderboard_level7_button);
        level7LeaderBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeaderboard(getString(R.string.leaderboard_level_7));
            }
        });
        Button level8LeaderBoardButton = findViewById(R.id.leaderboard_level8_button);
        level8LeaderBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeaderboard(getString(R.string.leaderboard_level_8));
            }
        });
        Button level9LeaderBoardButton = findViewById(R.id.leaderboard_level9_button);
        level9LeaderBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeaderboard(getString(R.string.leaderboard_level_9));
            }
        });

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
            Toast toast = Toast.makeText(this, "sign in success", Toast.LENGTH_LONG);
            toast.show();
            signInButton.setVisibility(View.GONE);
            signOutButton.setVisibility(View.VISIBLE);
        } catch (ApiException e) {
            Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
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
        pagesInfo.putString(SelectModeActivity.GAME_MODE_KEY, SelectModeActivity.LEVEL_MODE);
        pagesInfo.putInt(LEVEL_KEY, level);
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
