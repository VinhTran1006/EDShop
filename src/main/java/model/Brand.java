/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * 
 */
public class Brand {
    private int brandId;
    private String brandName;
    private String imgUrlLogo;
    private boolean isActive;

    public Brand() {
    }

    public Brand(int brandId, String brandName, String imgUrlLogo, boolean isActive) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.imgUrlLogo = imgUrlLogo;
        this.isActive = isActive;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getImgUrlLogo() {
        return imgUrlLogo;
    }

    public void setImgUrlLogo(String imgUrlLogo) {
        this.imgUrlLogo = imgUrlLogo;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }



   
}
