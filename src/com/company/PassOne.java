package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PassOne {
    File f;
    Scanner input;
    String [] sym = new String[100];
    String [] ins = new String[100];
    String [] lab = new String[100];
    String [] loc = new String[100];
    String [] symLocS = new String[100];
    String [] symLocL = new String[100];
    String format = new String();
    int i;


    public PassOne(File file) throws FileNotFoundException{
        f = file;
        input = new Scanner(f);

    }
    public void split(){
        i =0;
        while(input.hasNext()){
            sym[i] = input.next();
            ins[i] = input.next();
            lab[i] = input.next();
            i++;
        }
    }
    public void locate(){
        int j,total,curr,prev=0;
        for(j=0;j<i;j++){
            if(ins[j].equals("START")){
                loc[j] = lab[j];
                loc[j+1] = lab[j];
                loc[j+1] = lab[j];
                total = Integer.parseInt(lab[j],16);
                prev=total;
            }else if(ins[j].equals("RESW")){
                curr=3*Integer.valueOf(lab[j]);
                total=curr+prev;
                loc[j+1] =Integer.toHexString(total);
                prev=total;
            }else if(ins[j].equals("RESB")){
                curr=Integer.valueOf(lab[j]);
                total=curr+prev;
                loc[j+1] =Integer.toHexString(total);
                prev=total;
            }else if(ins[j].equals("BYTE")){
                if(lab[j].charAt(0)=='X'){
                    curr=(lab[j].length()-3)/2;
                    total=curr+prev;
                    loc[j+1] =Integer.toHexString(total);
                    prev=total;
                }else if(lab[j].charAt(0)=='C'){
                    curr=lab[j].length()-3;
                    total=curr+prev;
                    loc[j+1] =Integer.toHexString(total);
                    prev=total;
                }
            }else if(ins[j].charAt(0)=='+'){
                curr=4;
                total=curr+prev;
                loc[j+1] =Integer.toHexString(total);
                prev=total;
            }else if(ins[j].equals("BASE")){
                curr=0;
                total=curr+prev;
                loc[j+1] =Integer.toHexString(total);
                prev=total;
            }else{
                int flag = 0;
                for(int k=0;k<59 && flag==0;k++){
                    if(ins[j].equals(Converter.OPTAB[k][0])){
                        format = Converter.OPTAB[k][1];
                        flag = 1;
                    }
                }
                flag =0;
                if(format.equals("1")){
                    curr=1;
                    total=curr+prev;
                    loc[j+1] =Integer.toHexString(total);
                    prev=total;
                }else if(format.equals("2")){
                    curr=2;
                    total=curr+prev;
                    loc[j+1] =Integer.toHexString(total);
                    prev=total;
                }else{
                    String[] s = new String[10];
                    s = lab[j].split(",");
                    if(s.length>1&& ins[j].equals("WORD")){
                        curr=3*s.length;
                        total=curr+prev;
                        loc[j+1] =Integer.toHexString(total);
                        prev=total;
                    }else{
                        curr=3;
                        total=curr+prev;
                        loc[j+1] =Integer.toHexString(total);
                        prev=total;
                    }
                }
            }
        }
        for(int n=0;n<i;n++){
            System.out.println(loc[n]+"\t"+sym[n]+"\t"+ins[n]+"\t"+lab[n]+"\t");
        }
        System.out.println("\n-----------------------------------------");
    }
    public void viewSymbolTable(){
        System.out.println("\nSymbol\tLocation\n");
        for(int j=0;j<i;j++){
            if(!sym[j].equals(";")){
                if((!ins[j].equals("START"))){
                    symLocS[j] = sym[j];
                    symLocL[j]= loc[j];
                    System.out.println(symLocS[j]+"\t"+symLocL[j]);
                }
            }
        }
        System.out.println("\n================================================================================\n");
    }
}

