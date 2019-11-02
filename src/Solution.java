import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.AbstractCollection;
import java.io.FilterInputStream;
import java.io.BufferedInputStream;
import java.util.LinkedList;
import java.util.ArrayList;
import java.io.InputStream;



public class Main {
    public static void main(String[] ARGS) {
        new Thread(null, new Runnable() {
            public void run() {
                new Main().solve();
            }
        }, "1", 1 << 26).start();
    }
    
    void solve() {
        InputStream IS = System.in;
        OutputStream OS = System.out;
        ScanReader in = new ScanReader(IS);
        PrintWriter out = new PrintWriter(OS);
        Solution stronglyconnectedcomponent = new Solution();
        stronglyconnectedcomponent.solve(1, in, out);
        out.close();
    }
    
    static class Solution {
        private  boolean[] discovered;
    private  LinkedList<Integer> finishTimeOrder;
    private  int n;
    private  int m;
    private  int finishTimeCount = 0;
    private  ArrayList<Integer>[] edgesMap ;
    private  ArrayList<Integer>[] reverseEdgesMap ;
    private boolean[] ans;
    private long count = 0;
        
    public void solve(int testNumber, ScanReader in, PrintWriter out){
        
        n = in.scanInt();
        
        edgesMap = new ArrayList[n+1];
        reverseEdgesMap = new ArrayList[n+1];
        ans = new boolean[n + 1];
        
        for (int i = 0; i <= n; i++) {
                edgesMap[i] = new ArrayList<>();
                reverseEdgesMap[i] = new ArrayList<>();
            }
        
        discovered = new boolean[n+1];
        for(int i = 0;i<n+1;i++){
            discovered[i] = false;
        }
        finishTimeOrder = new LinkedList<>();
        int cost[] = new int[n+1];
        for(int i = 1; i<=n;i++){
           // cost[i] = scanner.nextInt();
           // System.out.println(cost[i]);
        }
        // m = scanner.nextInt();
        m= in.scanInt();
        //System.out.println(m);
        
        
        for(int i = 0;i<m;i++){
            //int key  = scanner.nextInt();
            //int value = scanner.nextInt();
            int key = in.scanInt();
            int value = in.scanInt();
           // edgesMap =  solution.updateAdjacencyList(edgesMap, key, value);
           // reverseEdgesMap = solution.updateAdjacencyList(reverseEdgesMap, value, key);
           edgesMap[key].add(value);
           reverseEdgesMap[value].add(key);
        }
        //solution.printHashMap(edgesMap);
        //solution.printHashMap(reverseEdgesMap);
        runDFSAndCalculateFinishTime();
        HashMap<Integer,ArrayList<Integer>> scc = createStronglyConnectedComponents();
        //solution.printHashMap(scc);
        int[] totalCost  = calculateTotalCost(scc, cost);
        for(int i = 1; i<n+1;i++){
            if(ans[i] == true){
            out.print("1 ");
            }else {
                out.print("0 ");
            }
        }
        
    } 
    
    public HashMap<Integer,ArrayList<Integer>> updateAdjacencyList(HashMap<Integer,ArrayList<Integer>> edgesMap, int key, int value){
        if(edgesMap.containsKey(key)){
                ArrayList<Integer> prevValues = edgesMap.get(key);  
                prevValues.add(value);
                edgesMap.put(key, prevValues);
            }
            else {
                ArrayList<Integer> prevValues = new ArrayList<>();    
                prevValues.add(value);
                edgesMap.put(key, prevValues);
            }
                
        return edgesMap;
    }
    
    public void printHashMap(HashMap<Integer,ArrayList<Integer>> edgesMap){
        for(Integer eachkey: edgesMap.keySet()){
            System.out.print(eachkey+"--->");
            for(Integer eachValue : edgesMap.get(eachkey)){
                System.out.print(eachValue+" ");
            }
            System.out.println();
        }
    }
    
    public  void runDFSAndCalculateFinishTime(){
        for(int i = 1; i<=n;i++){
            if(!discovered[i]){
                runDFSonReverse( i, true);
            }
        }
    }
    
    public  void runDFSonReverse( int i, boolean UpdateFinishTime ){
        
        discovered[i] = true;
        
       /* for(Integer eachChild : reverseEdgesMap[i]){
            if(discovered[eachChild] == false){
             runDFSonReverse(eachChild, UpdateFinishTime);
            
            }
        } */
        for (int j = 0; j < reverseEdgesMap[i].size(); j++) {
                int t = reverseEdgesMap[i].get(j);
                if (!discovered[t]) {
                    runDFSonReverse(t, UpdateFinishTime);
                }
            }
        
        if(UpdateFinishTime){
        finishTimeOrder.add(i);
        }
        return ;
    }
    
    public  ArrayList<Integer> runDFS( int i, boolean UpdateFinishTime ){
        
        ArrayList<Integer> temp = new ArrayList<>();
        
        if(discovered[i] == true){
            return temp;
        }
        count++;
        temp.add(i);
        discovered[i] = true;
        
        for(Integer eachChild : edgesMap[i]){
            if(discovered[eachChild] == false){
            ArrayList<Integer> newTemp = runDFS(eachChild, UpdateFinishTime);
            temp.addAll(newTemp);
            }
        }
        
        if (count > 1) {
                ans[i] = true;
            }
        return temp;
    }
    
    public  HashMap<Integer,ArrayList<Integer>> createStronglyConnectedComponents(){
        for(int i = 0; i<=n;i++){
            discovered[i] = false;
        }
        int count = 0;
        HashMap<Integer, ArrayList<Integer>> scc = new HashMap<>();
        
       /* for(int i = n-1; i>=0;i--){
            if(discovered[finishTimeOrder[i]] == false){
                ArrayList<Integer> component = runDFS( finishTimeOrder[i], false);
                scc.put(count, component);
                count++;
            }
        }*/
        while (!finishTimeOrder.isEmpty()) {
                int t = finishTimeOrder.removeLast();
                if (!discovered[t]) {
                    count = 0;
                    ArrayList<Integer> component = runDFS(t,false);
                    scc.put(count, component);
                
                }
            }
        return scc;
    }
    
    public int[] calculateTotalCost(HashMap<Integer, ArrayList<Integer>> scc, int[] cost){
        int[] sum =  new int[n+1];
        for(Integer eachKey: scc.keySet()){
            ArrayList<Integer> sccValues = scc.get(eachKey);
            if(sccValues.size() > 1){
            for(Integer eachValue : sccValues){
                sum[eachValue] = 1;
            }
            }
        }
        return sum;
    }
    
    public  int getMinimum(ArrayList<Integer> component, int[] cost){
        int min = 1000000000;
        for(Integer eachValue : component){
            if(cost[eachValue] < min){
                min = cost[eachValue];
            }
        }
        return min;
    }
    
    
    }
    
    static class ScanReader {
        private byte[] buf = new byte[4 * 1024];
        private int index;
        private BufferedInputStream in;
        private int Total_Char;
 
        public ScanReader(InputStream inputStream) {
            in = new BufferedInputStream(inputStream);
        }
 
        private int scan() {
            if (index >= Total_Char) {
                index = 0;
                try {
                    Total_Char = in.read(buf);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Total_Char <= 0) return -1;
            }
            return buf[index++];
        }
 
        public int scanInt() {
            int integer = 0;
            int n = scan();
            while (isWhiteSpace(n)) n = scan();
            int neg = 1;
            if (n == '-') {
                neg = -1;
                n = scan();
            }
            while (!isWhiteSpace(n)) {
                if (n >= '0' && n <= '9') {
                    integer *= 10;
                    integer += n - '0';
                    n = scan();
                }
            }
            return neg * integer;
        }
 
        private boolean isWhiteSpace(int n) {
            if (n == ' ' || n == '\n' || n == '\r' || n == '\t' || n == -1) return true;
            else return false;
        }
 
    }
    


    
    
}