package com.iinterchange

class NutritionDetails {
    String nutrient 
    int total 
    int goal 
    String metric
    static constraints = {
        metric blank:true
    }
}
