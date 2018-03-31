package com.beichenzhang.calculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import android.content.Context;

import org.mozilla.javascript.Scriptable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    private SignInButton Signin;
    private GoogleApiClient client;
    private static final int REQ_CODE=9001;
//Declare number Button.
    private Button nine;
    private Button eight;
    private Button seven;
    private Button six;
    private Button five;
    private Button four;
    private Button three;
    private Button two;
    private Button one;
    private Button zero;
//Declare other Button.
    private Button dot;
    private Button clear;
    private Button equal;
    private Button mul;
    private Button add;
    private Button div;
    private Button sub;
    private Button sin;
    private Button cos;
    private Button tan;
    private Button par;
    private Button logout;
    private Button backspace;
//Declare monitor text.
    private TextView proc;
    private TextView result;
    private ImageView icon;
//Helper variable
    String proccesor;
    int isInPar;     //check how many parenthesis are open at this time. eg 1+((( has isInPar = 3.
    boolean have_Function; //whether can append operator or sin(),cos() and tan() next.
    boolean start;  //whether it is the start of the String
    boolean ispar; //restrict number after ')'. like (1+1)2.
    boolean negative; //negative number
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//Initialization
        have_Function=false;
        start=true;
        ispar=false;
        negative=false;
        isInPar = 0;

        Signin = (SignInButton)findViewById(R.id.bn_login);
        Signin.setOnClickListener(this);

        icon = (ImageView)findViewById(R.id.imageView);
        nine = (Button)findViewById(R.id.button2);
        nine.setOnClickListener(this);
        eight = (Button)findViewById(R.id.button1);
        eight.setOnClickListener(this);
        seven = (Button)findViewById(R.id.button3);
        seven.setOnClickListener(this);
        six = (Button)findViewById(R.id.button4);
        six.setOnClickListener(this);
        five = (Button)findViewById(R.id.button5);
        five.setOnClickListener(this);
        four = (Button)findViewById(R.id.button6);
        four.setOnClickListener(this);
        three = (Button)findViewById(R.id.button9);
        three.setOnClickListener(this);
        two = (Button)findViewById(R.id.button8);
        two.setOnClickListener(this);
        one = (Button)findViewById(R.id.button7);
        one.setOnClickListener(this);
        zero = (Button)findViewById(R.id.button0);
        zero.setOnClickListener(this);
        dot = (Button)findViewById(R.id.buttondot);
        dot.setOnClickListener(this);
        clear = (Button)findViewById(R.id.buttoncl);
        clear.setOnClickListener(this);
        equal = (Button)findViewById(R.id.button14);
        equal.setOnClickListener(this);
        add = (Button)findViewById(R.id.button10);
        add.setOnClickListener(this);
        sub = (Button)findViewById(R.id.button13);
        sub.setOnClickListener(this);
        mul = (Button)findViewById(R.id.button12);
        mul.setOnClickListener(this);
        div = (Button)findViewById(R.id.button11);
        div.setOnClickListener(this);
        sin = (Button)findViewById(R.id.button15);
        sin.setOnClickListener(this);
        cos = (Button)findViewById(R.id.button16);
        cos.setOnClickListener(this);
        tan = (Button)findViewById(R.id.button17);
        tan.setOnClickListener(this);
        par = (Button)findViewById(R.id.button18);
        par.setOnClickListener(this);
        logout = (Button)findViewById(R.id.button19);
        logout.setOnClickListener(this);
        backspace = (Button)findViewById(R.id.button20);
        backspace.setOnClickListener(this);
        proc = (TextView) findViewById(R.id.textView);
        result = (TextView) findViewById(R.id.textView2);
//Hide button when User have not login.
        proc.setText("");
        result.setText("");
        nine.setVisibility(View.GONE);
        eight.setVisibility(View.GONE);
        seven.setVisibility(View.GONE);
        six.setVisibility(View.GONE);
        five.setVisibility(View.GONE);
        four.setVisibility(View.GONE);
        three.setVisibility(View.GONE);
        two.setVisibility(View.GONE);
        one.setVisibility(View.GONE);

        zero.setVisibility(View.GONE);
        dot.setVisibility(View.GONE);
        clear.setVisibility(View.GONE);
        equal.setVisibility(View.GONE);
        add.setVisibility(View.GONE);
        sub.setVisibility(View.GONE);
        mul.setVisibility(View.GONE);
        div.setVisibility(View.GONE);
        sin.setVisibility(View.GONE);
        cos.setVisibility(View.GONE);
        tan.setVisibility(View.GONE);
        proc.setVisibility(View.GONE);
        result.setVisibility(View.GONE);
        par.setVisibility(View.GONE);
        icon.setVisibility(View.GONE);
        logout.setVisibility(View.GONE);
        backspace.setVisibility(View.GONE);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        client = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
//**********************************************************
//Handling the botton. Eliminate some illegal input.
//**********************************************************
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bn_login:
                signin();
                break;
            case R.id.button7:
                if(!ispar) {
                    proccesor = proc.getText().toString();
                    proc.setText(proccesor + "1");
                    have_Function = false;
                    start = false;
                }
                break;
            case R.id.button8:
                if(!ispar) {
                    proccesor = proc.getText().toString();
                    proc.setText(proccesor + "2");
                    have_Function = false;
                    start = false;
                }
                break;
            case R.id.button9:
                if(!ispar) {
                    proccesor = proc.getText().toString();
                    proc.setText(proccesor + "3");
                    start = false;
                    have_Function = false;
                }
                break;
            case R.id.button6:
                if(!ispar) {
                    proccesor = proc.getText().toString();
                    proc.setText(proccesor + "4");
                    start = false;
                    have_Function = false;
                }
                break;
            case R.id.button5:
                if(!ispar) {
                    proccesor = proc.getText().toString();
                    proc.setText(proccesor + "5");
                    have_Function = false;
                    start = false;
                }
                break;
            case R.id.button4:
                if(!ispar) {
                    proccesor = proc.getText().toString();
                    proc.setText(proccesor + "6");
                    have_Function = false;
                    start = false;
                }
                break;
            case R.id.button3:
                if(!ispar) {
                    proccesor = proc.getText().toString();
                    proc.setText(proccesor + "7");
                    have_Function = false;
                    start = false;
                }
                break;
            case R.id.button1:
                if(!ispar) {
                    proccesor = proc.getText().toString();
                    proc.setText(proccesor + "8");
                    have_Function = false;
                    start = false;
                }
                break;
            case R.id.button2:
                if(!ispar) {
                    proccesor = proc.getText().toString();
                    proc.setText(proccesor + "9");
                    have_Function = false;
                    start = false;
                }
                break;
            case R.id.button0:
                if(!ispar) {
                    proccesor = proc.getText().toString();
                    proc.setText(proccesor + "0");
                    have_Function = false;
                    start = false;
                }
                break;
            case R.id.buttoncl:
                proc.setText("");
                result.setText("");
                have_Function=true;
                start=true;
                ispar=false;
                break;
            case R.id.button10:
                if (!have_Function) {
                    proccesor = proc.getText().toString();
                    proc.setText(proccesor + "+");
                    have_Function = true;
                    ispar = false;
                    negative = false;
                }
                break;
            case R.id.button13:
                if (!have_Function||!negative) {
                    proccesor = proc.getText().toString();
                    proc.setText(proccesor + "-");
                    have_Function = true;
                    ispar = false;
                    if(have_Function) {
                        negative=true;
                    }
                }
                break;
            case R.id.button12:
                if (!have_Function) {
                    proccesor = proc.getText().toString();
                    proc.setText(proccesor + "*");
                    have_Function = true;
                    ispar = false;
                    negative = true;
                }
                break;
            case R.id.button11:
                if (!have_Function) {
                    proccesor = proc.getText().toString();
                    proc.setText(proccesor + "/");
                    have_Function = true;
                    ispar = false;
                    negative = false;
                }
                break;
            case R.id.buttondot:
                if (!have_Function&&!ispar) {
                    proccesor = proc.getText().toString();
                    proc.setText(proccesor + ".");
                    have_Function = true;
                    break;
                }
                break;
            case R.id.button18:
                if (!have_Function) {
                    if (isInPar != 0) {
                        proccesor = proc.getText().toString();
                        proc.setText(proccesor + ")");
                        isInPar -= 1;
                        have_Function = false;
                        ispar=true;
                    }

                }
                break;
            case R.id.button15:
                if (have_Function||start) {
                    proccesor = proc.getText().toString();
                    proc.setText(proccesor + "Sin(");
                    isInPar += 1;
                    have_Function = true;
                    negative=false;

                }
                break;
            case R.id.button16:
                if (have_Function||start) {
                    proccesor = proc.getText().toString();
                    proc.setText(proccesor + "Cos(");
                    isInPar += 1;
                    have_Function = true;
                    negative=false;
                }
                break;
            case R.id.button17:
                if (have_Function||start) {
                    proccesor = proc.getText().toString();
                    proc.setText(proccesor + "Tan(");
                    isInPar += 1;
                    have_Function = true;
                    negative=false;
                }
                break;
            case R.id.button14:
                proccesor = proc.getText().toString();
                //calculate result from given string.
                String resu = evaluate(proccesor);

                result.setText(compute(resu));

                break;
            case R.id.button19:
                signout();
                break;
            case R.id.button20:
                proccesor = proc.getText().toString();
                proccesor = proccesor.substring(0, proccesor.length() - 1);
                proc.setText(proccesor);
                break;
        }
    }
//**********************************************************
//param: String to compute
//return: result string
//Using rhino to do computation.
//**********************************************************
    public String compute(String a){
        org.mozilla.javascript.Context rhino = new org.mozilla.javascript.Context().enter();
        rhino.setOptimizationLevel(-1);
        String resu = "";
        try{
            Scriptable scope = rhino.initStandardObjects();
            resu = rhino.evaluateString(scope, a,"JavaScript",1,null).toString();

        }
        catch(Exception e)
        {
            resu = "Error";
        }
        return resu;
    }
//**********************************************************
//param: String input;
//       int    index;
//return: int  the next index.
//Helper function of  evaluate(String ..). Helps to find to next index to
// process when met sin(),cos(),tan() in the string
//**********************************************************
    public int skip (String input, int index){
        int theend=1;
        int ret_val=0;
        int size= input.length();
        for (int i =index+4; i<size;i++){
            if (theend ==0){
                ret_val = i;
                break;
            }
            if (input.charAt(i)==')'){
                theend-=1;
            }
            if (input.charAt(i)=='('){
                theend+=1;
            }

        }
        if (ret_val==0){
            return size;
        }
        return ret_val-1;
    }
//**********************************************************
//param: String input;
//return: String with no Sin(), Cos(), Tan().
//Pre-process the string by Calculating Sin(), Cos() and Tan() in advanced.
// Recursively called for each Sin(), Cos(), Tan().
//**********************************************************
    public String evaluate (String input){
        int size = input.length();
        String ret_val = "";
        for (int i =0; i <size;i++){
            if (input.charAt(i)=='S'){
                String part = evaluate (input.substring(i+4));
                String calc = compute(part);
                if (!calc.equals("Error")) {
                    Double part_num =Math.sin(Double.parseDouble(calc));
                    int next_index = skip(input,i);
                    i = next_index;
                    ret_val += part_num.toString();
                    if(i>=size){
                        break;
                    }
                }
                else{
                    return "Error1";
                }
            }
            else if (input.charAt(i)=='C'){
                String part = evaluate (input.substring(i+4));
                String calc = compute(part);
                if (!calc.equals("Error")) {
                    Double part_num =Math.cos(Double.parseDouble(calc));
                    int next_index = skip(input,i);
                    i = next_index;
                    ret_val += part_num.toString();
                    if(i>=size){
                        break;
                    }
                }
                else{
                    return "Error1";
                }
            }
            else if (input.charAt(i)=='T'){
                String part = evaluate (input.substring(i+4));
                String calc = compute(part);
                if (!calc.equals("Error")) {
                    Double part_num =Math.cos(Double.parseDouble(calc));
                    int next_index = skip(input,i);
                    i = next_index;
                    ret_val += part_num.toString();
                    if(i>=size){
                        break;
                    }
                }
                else{
                    return "Error1";
                }
            }
            else if (input.charAt(i)==')') {
                break;
            }
            else{
                ret_val= ret_val+ input.charAt(i);
            }

        }
        return ret_val;
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signin(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(client);
        startActivityForResult(intent,REQ_CODE);
    }
    private void signout(){
        Auth.GoogleSignInApi.signOut(client).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                update(false);
            }
        });
    }
//Check if log in successfully from Google.
    private void HandleResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            update(true);
        }
        else {
            update(false);
        }
    }
//update the page based on log in status
    private void update(boolean isLogin){
        //Show all the hidden button and text when login.
        //Hide the Sign in button.
        if (isLogin){
            nine.setVisibility(View.VISIBLE);
            eight.setVisibility(View.VISIBLE);
            seven.setVisibility(View.VISIBLE);
            six.setVisibility(View.VISIBLE);
            five.setVisibility(View.VISIBLE);
            four.setVisibility(View.VISIBLE);
            three.setVisibility(View.VISIBLE);
            two.setVisibility(View.VISIBLE);
            one.setVisibility(View.VISIBLE);
            Signin.setVisibility(View.GONE);

            zero.setVisibility(View.VISIBLE);
            dot.setVisibility(View.VISIBLE);
            clear.setVisibility(View.VISIBLE);
            equal.setVisibility(View.VISIBLE);
            add.setVisibility(View.VISIBLE);
            sub.setVisibility(View.VISIBLE);
            mul.setVisibility(View.VISIBLE);
            div.setVisibility(View.VISIBLE);
            sin.setVisibility(View.VISIBLE);
            cos.setVisibility(View.VISIBLE);
            tan.setVisibility(View.VISIBLE);
            proc.setVisibility(View.VISIBLE);
            result.setVisibility(View.VISIBLE);
            par.setVisibility(View.VISIBLE);
            icon.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
            backspace.setVisibility(View.VISIBLE);
        }
        else{
            nine.setVisibility(View.GONE);
            eight.setVisibility(View.GONE);
            seven.setVisibility(View.GONE);
            six.setVisibility(View.GONE);
            five.setVisibility(View.GONE);
            four.setVisibility(View.GONE);
            three.setVisibility(View.GONE);
            two.setVisibility(View.GONE);
            one.setVisibility(View.GONE);
            Signin.setVisibility(View.VISIBLE);

            zero.setVisibility(View.GONE);
            dot.setVisibility(View.GONE);
            clear.setVisibility(View.GONE);
            equal.setVisibility(View.GONE);
            add.setVisibility(View.GONE);
            sub.setVisibility(View.GONE);
            mul.setVisibility(View.GONE);
            div.setVisibility(View.GONE);
            sin.setVisibility(View.GONE);
            cos.setVisibility(View.GONE);
            tan.setVisibility(View.GONE);
            proc.setVisibility(View.GONE);
            result.setVisibility(View.GONE);
            par.setVisibility(View.GONE);
            icon.setVisibility(View.GONE);
            logout.setVisibility(View.GONE);
            backspace.setVisibility(View.GONE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==REQ_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            HandleResult(result);
        }
    }
}
