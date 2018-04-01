package com.beichenzhang.calculator;

import org.mozilla.javascript.Scriptable;

/**
 * Created by 87279 on 4/1/2018.
 */



public class Arithmatic {
//Helper function to find the start of last number or end of next number
//after= 1, find next, after=0,find last.
    public int find_num (String input, int index, int after ){
        int dot_count=0;
        int result = -1;
        if (after==1){
            for (int i = index+1;i<input.length();i++){
                if (input.charAt(i)=='*'||input.charAt(i)=='/'||input.charAt(i)=='+'){
                    result = i;
                    break;
                    }
                else if((i!=0)&&(input.charAt(i)=='-')&&((input.charAt(i-1)!='-')&&(input.charAt(i-1)!='+')
                        &&(input.charAt(i-1)!='*')&&(input.charAt(i-1)!='/'))) {
                    result = i;
                    break;
                }
                else if(input.charAt(i)=='.'){
                    dot_count+=1;
                }
                if (dot_count>1){
                    return -1;
                }
            }
            if (result ==-1){
                result = input.length()-1;
            }
            else{result-=1;}
        }
        else{

            for (int i = index-1;i>=0;i--){
                if (input.charAt(i)=='*'||input.charAt(i)=='/'||input.charAt(i)=='+'){
                    result = i;
                    break;
                }
                else if((i!=0)&&(input.charAt(i)=='-')&&((input.charAt(i-1)!='-')&&(input.charAt(i-1)!='+')
                        &&(input.charAt(i-1)!='*')&&(input.charAt(i-1)!='/'))) {
                    result = i;
                    break;
                }
                else if(input.charAt(i)=='.'){
                    dot_count+=1;
                }
                if (dot_count>1){
                    return -1;
                }
            }
            if (result ==-1){
                result = 0;
            }
            else{result+=1;}
        }
        return result;
    }
//calculate partial result of number on the left of index and the number on the right of index.
//mul =1, do multiplication, mul=0 do divide.
    public double calc(String input, int index, int mul ){
        double result = 0.0;
        if (mul==1){
            int first_index= find_num(input,index, 0);
            int second_index=find_num(input,index,1);
            if((first_index<0) ||(second_index<0)){
                return -9988.2323;
            }
            double first = Double.parseDouble(input.substring(first_index,index));
            double second= Double.parseDouble(input.substring(index+1,second_index+1));
            result = first*second;
        }
        else{
            int first_index= find_num(input,index, 0);
            int second_index=find_num(input,index,1);
            if((first_index<0)||(second_index<0)){
                return -9988.2323;
            }
            double first = Double.parseDouble(input.substring(first_index,index));
            double second= Double.parseDouble(input.substring(index+1,second_index+1));
            result = first/second;
        }
        return result;
    }
//calculate multiplication and division
    public  String Handle_mul_div(String a){
        double cumulate = 0.0;
        int index_of_mul = a.indexOf('*');
        int index_of_div = a.indexOf('/');
        if (a.charAt(a.length()-1)=='*'||a.charAt(a.length()-1)=='/'||a.charAt(a.length()-1)=='+'||a.charAt(a.length()-1)=='-'){
            return "Error";
        }
        while ((index_of_div!=-1) || (index_of_mul !=-1)){
            if ((index_of_div!=-1) && (index_of_mul !=-1)){
                if (index_of_div > index_of_mul){
                    cumulate = calc(a,index_of_mul,1);
                    if (cumulate ==-9988.2323){
                        return "Error";
                    }
                    int before = find_num(a,index_of_mul,0);
                    int after = find_num(a,index_of_mul,1);
                    a = a.substring(0,before)+ Double.toString(cumulate)+a.substring(after+1,a.length());
                    return Handle_mul_div(a);
                }
                else{
                    cumulate = calc(a,index_of_div,0);
                    if (cumulate ==-9988.2323){
                        return "Error";
                    }
                    int before = find_num(a,index_of_div,0);
                    int after = find_num(a,index_of_div,1);
                    a = a.substring(0,before)+ Double.toString(cumulate)+a.substring(after+1,a.length());
                    return Handle_mul_div(a);
                }
            }
            else if (index_of_div ==-1 ){
                cumulate = calc(a,index_of_mul,1);
                if (cumulate ==-9988.2323){
                    return "Error";
                }
                int before = find_num(a,index_of_mul,0);
                int after = find_num(a,index_of_mul,1);
                a = a.substring(0,before)+ Double.toString(cumulate)+a.substring(after+1,a.length());
                return Handle_mul_div(a);
            }
            else{
                cumulate = calc(a,index_of_div,0);
                if (cumulate ==-9988.2323){
                    return "Error";
                }
                int before = find_num(a,index_of_div,0);
                int after = find_num(a,index_of_div,1);
                a = a.substring(0,before)+ Double.toString(cumulate)+a.substring(after+1,a.length());
                return Handle_mul_div(a);
            }
        }
        return a;
    }
//calculate addition and subtraction.
    public  String Handle_add_sub(String a){
        double result=0.0;
        String num = "";
        int dot = 0;
        if (a.charAt(a.length()-1)=='+'||a.charAt(a.length()-1)=='-'){
            return "Error";
        }
        for(int i=0;i<a.length();i++ ){
            if (dot>1){
                return "Error";
            }
            if (a.charAt(i)=='+'){
                if (dot>1){
                    return "Error";
                }
                dot = 0;
                double number = Double.parseDouble(num);
                String num_recur = Handle_add_sub(a.substring(i+1,a.length()));
                if (num_recur.equals("Error")){
                    return "Error";
                }
                else{
                    number+=Double.parseDouble(num_recur);
                    return Double.toString(number);
                }
            }
            else if ((i!=0)&&(a.charAt(i)=='-')&&((a.charAt(i-1)!='-')||(a.charAt(i)!='+'))){
                if (dot>1){
                    return "Error";
                }
                dot = 0;
                double number = Double.parseDouble(num);
                String num_recur = Handle_add_sub(a.substring(i+1,a.length()));
                if (num_recur.equals("Error")){
                    return "Error";
                }
                else{
                    number-=Double.parseDouble(num_recur);
                    return Double.toString(number);
                }
            }
            else if (a.charAt(i)=='.'){
                dot+=1;
                if (dot>1){
                    return "Error";
                }
                num+=a.charAt(i);
            }
            else{
                num+=a.charAt(i);
            }
        }
        return num;
    }

    //**********************************************************
//param: String to compute
//return: result string
//Do the add_sub after mul_div.
//**********************************************************
    public String compute(String a){
        String after = Handle_mul_div(a);
        String result= Handle_add_sub(after);
        return result;
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
    public String evaluate (String input, boolean inRad){
        int size = input.length();
        String ret_val = "";
        for (int i =0; i <size;i++){
            if (input.charAt(i)=='S'){

                String part = evaluate (input.substring(i+4),inRad);
                String calc = compute(part);
                if (!calc.equals("Error")) {
                    Double part_num = 0.0;
                    if  (inRad) {
                        part_num = Math.sin(Double.parseDouble(calc));
                    }
                    else{
                        part_num = Math.sin(Math.toRadians(Double.parseDouble(calc)));
                    }
                    int next_index = skip(input,i);
                    i = next_index;
                    ret_val += part_num.toString();
                    if(i>=size){
                        break;
                    }
                }
                else{
                    return "Error";
                }
            }
            else if (input.charAt(i)=='C'){
                String part = evaluate (input.substring(i+4),inRad);
                String calc = compute(part);
                if (!calc.equals("Error")) {
                    Double part_num = 0.0;
                    if  (inRad) {
                        part_num = Math.cos(Double.parseDouble(calc));
                    }
                    else{
                        part_num = Math.cos(Math.toRadians(Double.parseDouble(calc)));
                    }
                    int next_index = skip(input,i);
                    i = next_index;
                    ret_val += part_num.toString();
                    if(i>=size){
                        break;
                    }
                }
                else{
                    return "Error";
                }
            }
            else if (input.charAt(i)=='T'){
                String part = evaluate (input.substring(i+4),inRad);
                String calc = compute(part);
                if (!calc.equals("Error")) {
                    Double part_num = 0.0;
                    if  (inRad) {
                        part_num = Math.tan(Double.parseDouble(calc));
                    }
                    else{
                        part_num = Math.tan(Math.toRadians(Double.parseDouble(calc)));
                    }
                    int next_index = skip(input,i);
                    i = next_index;
                    ret_val += part_num.toString();
                    if(i>=size){
                        break;
                    }
                }
                else{
                    return "Error";
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

}
