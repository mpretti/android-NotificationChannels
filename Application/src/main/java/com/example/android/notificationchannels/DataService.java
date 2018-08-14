package com.example.android.notificationchannels;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import java.net.*;
import java.io.*;
import java.util.*;
import org.stellar.sdk.Server;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.KeyPair;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DataService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.android.notificationchannels.action.FOO";
    private static final String ACTION_BAZ = "com.example.android.notificationchannels.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.android.notificationchannels.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.android.notificationchannels.extra.PARAM2";
    public static final String CREATE_ACCOUNT_DataService = "com.example.android.notificationchannels.RESPONSE";

    public static final String EXTRA_KEY_OUT = "EXTRA_OUT";
    public DataService() {
        super("DataService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context) {
        Intent intent = new Intent(context, DataService.class);
        intent.setAction(ACTION_FOO);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DataService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("onHandleIntent !!!!!!!!");
        if (intent != null) {
            final String action = intent.getAction();
            System.out.println("action:");
            System.out.println(action);
            handleActionFoo();
//            } else if (ACTION_BAZ.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
//                handleActionBaz(param1, param2);
//            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo() {
        // TODO: Handle action Foo
        System.out.println("Handling action FOO");
        KeyPair pair = KeyPair.random();

        System.out.println(new String(pair.getSecretSeed()));
        System.out.println(pair.getAccountId());

        String friendbotUrl = String.format( "https://friendbot.stellar.org/?addr=%s", pair.getAccountId());
        try {
            InputStream response = new URL(friendbotUrl).openStream();
            String body = new Scanner(response, "UTF-8").useDelimiter("\\A").next();
            System.out.println("SUCCESS! You have a new account :)\n" + body);

            Server server = new Server("https://horizon-testnet.stellar.org");
            AccountResponse account = server.accounts().account(pair);
            System.out.println("Balances for account " + pair.getAccountId());
            for (AccountResponse.Balance balance : account.getBalances()) {
                System.out.println(String.format(
                        "Type: %s, Code: %s, Balance: %s",
                        balance.getAssetType(),
                        balance.getAssetCode(),
                        balance.getBalance()));
                Intent intentResponse = new Intent();
                intentResponse.setAction(CREATE_ACCOUNT_DataService);
                intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
                intentResponse.putExtra(EXTRA_KEY_OUT, balance.getBalance());
                sendBroadcast(intentResponse);
            }
        } catch ( IOException ioe ) {
            ioe.printStackTrace();
        }

    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
