package usecase.practica4;

import material.map.HashTableMapDH;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.StreamSupport;

public class PearRegister {

    private HashTableMapDH<Product, Iterable<PearStore>> map;

    public PearRegister () { this.map = new HashTableMapDH<>(); }

    public void loadFile(String pathToFile){
        try {
            File file = new File(pathToFile);
            Scanner sc = new Scanner(file);
            int blocks = Integer.parseInt(sc.nextLine());

            for (int i = 0; i < blocks; i++) {

                String pLine = sc.nextLine();
                String[] pLineSplit = pLine.split(" ");

                Product product = new Product(pLineSplit[0], Integer.parseInt(pLineSplit[1]));
                List<PearStore> stores = new LinkedList<>();

                for (int j = 0; j < Integer.parseInt(pLineSplit[2]); j++) {

                    String sLine = sc.nextLine();
                    String[] sLineSplit = sLine.split(" ");

                    PearStore store = new PearStore(sLineSplit[0], Integer.parseInt(sLineSplit[1]));
                    store.setUnits(Integer.parseInt(sLineSplit[2]));
                    store.setScore(Double.parseDouble(sLineSplit[3]));

                    stores.add(store);
                }

                this.map.put(product, stores);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addProduct(Product product, Iterable<PearStore> stores){ this.map.put(product, stores); }

    public void addSalesInPearStore(Product product, PearStore store, int units, double score){
        Iterable<PearStore> stores = this.map.get(product);
        if (stores != null) {
            for (PearStore ps: stores) {
                if (ps.equals(store)) {
                    store.setUnits(units);
                    store.setScore(score);
                    return;
                }
            }
            throw new RuntimeException("There is not such store");
        }
        throw new RuntimeException("There is not such product");
    }

    public double getScoreOfProduct(Product product){
        Iterable<PearStore> stores = this.map.get(product);
        if (stores != null) {
            double score = 0.0;
            int nStores = 0;
            for (PearStore ps: stores) {
                score += ps.getScore();
                nStores++;
            }
            return score / nStores;
        }
        throw new RuntimeException("There is not such product");
    }

    public PearStore getGreatestSeller(Product product){
        Iterable<PearStore> stores = this.map.get(product);
        if (stores != null) {
            int maxProductsSold = -1;
            PearStore maxSeller = null;
            for (PearStore ps: stores) {
                if (ps.getUnits() > maxProductsSold) {
                    maxProductsSold = ps.getUnits();
                    maxSeller = ps;
                }
            }
            if (maxProductsSold != -1) {
                return maxSeller;
            }
            throw new RuntimeException("The product was not sold in any store");
        }
        throw new RuntimeException("There is not such product");
    }

    public int getUnits(Product product){
        Iterable<PearStore> stores = this.map.get(product);
        if (stores != null) {
            int unitsSold = 0;
            for (PearStore ps: stores) {
                unitsSold += ps.getUnits();
            }
            return unitsSold;
        }
        throw new RuntimeException("There is not such product");
    }

    public boolean productExists(Product product){
        return StreamSupport.stream(this.map.keys().spliterator(), false).anyMatch(product::equals);
    }

}