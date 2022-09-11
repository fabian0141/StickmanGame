package schroeti.fabian.stickman;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends Activity {

    private EditText et;
    private Socket socket;
    private Button con;
    private ImageButton top,right,bottom,left;
    private ImageButton shoot;
    private JoyStick stick;

    private boolean bTop,bRight,bBottom,bLeft;
    private boolean bShoot;

    private boolean changed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = (EditText) findViewById(R.id.adress);
        stick = (JoyStick) findViewById(R.id.joystick);

        top = (ImageButton) findViewById(R.id.top);
        top.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        bTop = true;
                        changed = true;
                        break;

                    case MotionEvent.ACTION_UP:
                        bTop = false;
                        changed = true;
                        break;
                }
                return true;
            }
        });

        right = (ImageButton) findViewById(R.id.right);
        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        bRight = true;
                        changed = true;
                        break;

                    case MotionEvent.ACTION_UP:
                        bRight = false;
                        changed = true;
                        break;
                }
                return true;
            }
        });

        bottom = (ImageButton) findViewById(R.id.bottom);
        bottom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        bBottom = true;
                        changed = true;
                        break;

                    case MotionEvent.ACTION_UP:
                        bBottom = false;
                        changed = true;
                        break;
                }
                return true;
            }
        });

        left = (ImageButton) findViewById(R.id.left);
        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        bLeft = true;
                        changed = true;
                        break;

                    case MotionEvent.ACTION_UP:
                        bLeft = false;
                        changed = true;
                        break;
                }
                return true;
            }
        });

        shoot = (ImageButton) findViewById(R.id.shoot);
        shoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        bShoot = true;
                        changed = true;
                        break;

                    case MotionEvent.ACTION_UP:
                        bShoot = false;
                        changed = true;
                        break;
                }
                return true;
            }
        });

        con = (Button) findViewById(R.id.connect);
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new ClientThread()).start();
            }
        });
    }

    class ClientThread implements Runnable {

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(et.getText().toString());

                socket = new Socket(serverAddr, 34568);

                con.post(new Runnable() {
                    public void run() {
                        con.setVisibility(View.GONE);
                        et.setVisibility(View.GONE);
                    }
                });

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PrintWriter out = null;
                        BufferedReader in = null;
                        try {
                            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
                            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        while(true) {
                            try {
                                    if(stick.changed || changed) {
                                        stick.changed = false;
                                        changed = false;
                                        out.println(bTop + " " + bRight + " " + bBottom + " " + bLeft + " " + bShoot + " " + stick.rot);
                                        in.readLine();
                                    } else {
                                        Thread.sleep(5);
                                    }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

    }
}
