package yummycherrypie.base_classes;

import java.util.HashMap;

/**
 * Created by Nikolay_Piskarev on 12/11/2015.
 */
public class Statistic {

    double sumCakePrice;
    double sumRecipePrice;
    int countBookings;
    int countBookingMen;
    int countTimeSpent;
    HashMap<Long, Integer> countProducts = new HashMap<Long, Integer>();//bookingTypeId, count
    HashMap<Long, Integer> countBookingTypesBookings = new HashMap<Long, Integer>();//bookingTypeId, count

    public double getSumCakePrice() {
        return sumCakePrice;
    }

    public void setSumCakePrice(double sumCakePrice) {
        this.sumCakePrice = sumCakePrice;
    }

    public double getSumRecipePrice() {
        return sumRecipePrice;
    }

    public void setSumRecipePrice(double sumRecipePrice) {
        this.sumRecipePrice = sumRecipePrice;
    }

    public int getCountBookings() {
        return countBookings;
    }

    public void setCountBookings(int countBookings) {
        this.countBookings = countBookings;
    }

    public int getCountBookingMen() {
        return countBookingMen;
    }

    public void setCountBookingMen(int countBookingMen) {
        this.countBookingMen = countBookingMen;
    }

    public int getCountTimeSpent() {
        return countTimeSpent;
    }

    public void setCountTimeSpent(int countTimeSpent) {
        this.countTimeSpent = countTimeSpent;
    }

    public HashMap<Long, Integer> getCountProducts() {
        return countProducts;
    }

    public void setCountProducts(HashMap<Long, Integer> countProducts) {
        this.countProducts = countProducts;
    }

    public void addCountProduct(long bookingTypeId, int count){
        countProducts.put(bookingTypeId, count);
    }
    public void addCountBookingTypesBookings(long bookingTypeId, int count){
        countBookingTypesBookings.put(bookingTypeId, count);
    }

    public int getCountBookingTypesBookings(long i) {
        return countBookingTypesBookings.get(i);
    }

    public void setCountBookingTypesBookings(HashMap<Long, Integer> countBookingTypesBookings) {
        this.countBookingTypesBookings = countBookingTypesBookings;
    }

    public void clearCountBookingTypesBookings(){
        countProducts.clear();
    }


    public void clearCountProduct(){
        countProducts.clear();
    }

    public Statistic(){}

    public Statistic( double sumRecipePrice, double sumCakePrice,int countBookings, int countTimeSpent){
        this.sumCakePrice = sumCakePrice;
        this.sumRecipePrice = sumRecipePrice;
        this.countBookings = countBookings;
        this.countTimeSpent = countTimeSpent;
    }

    public Statistic( double sumRecipePrice, double sumCakePrice,int countBookings, int countBookingMen, int countTimeSpent){
        this.sumCakePrice = sumCakePrice;
        this.sumRecipePrice = sumRecipePrice;
        this.countBookings = countBookings;
        this.countBookingMen = countBookingMen;
        this.countTimeSpent = countTimeSpent;
    }

    public Statistic(double sumRecipePrice, double sumCakePrice, int countBookings, int countBookingMen, int countTimeSpent, HashMap<Long, Integer> countProducts, HashMap<Long, Integer> countBookingTypesBookings){
        this.sumCakePrice = sumCakePrice;
        this.sumRecipePrice = sumRecipePrice;
        this.countBookings = countBookings;
        this.countBookingMen = countBookingMen;
        this.countTimeSpent = countTimeSpent;
        this.countProducts = countProducts;
        this.countBookingTypesBookings = countBookingTypesBookings;
    }
}
