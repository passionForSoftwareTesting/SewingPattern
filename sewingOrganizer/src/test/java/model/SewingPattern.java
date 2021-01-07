package model;

import java.io.File;
import java.util.List;

public class SewingPattern {

  private String brand;
  private int id;
  private String prodNumber;
  private String title;
  private String size;
  private List<String> clothingType;
  private String prodDescription;
  private String suggestedFabric;
  private String recommendedNotions;
  private List<String> linksToPhotos;
  private List<String> namesOfPhotos;
  private File photo;

  public SewingPattern withBrand(String brand) {
    this.brand = brand;
    return this;
  }

  public SewingPattern withProdNumber(String prodNumber) {
    this.prodNumber = prodNumber;
    return this;
  }

  public SewingPattern withId(int id) {
    this.id = id;
    return this;
  }

  public SewingPattern withTitle(String title) {
    this.title = title;
    return this;
  }

  public SewingPattern withSize(String size) {
    this.size = size;
    return this;
  }

  public SewingPattern withClothingType(List<String> clothingType) {
    this.clothingType = clothingType;
    return this;
  }

  public SewingPattern withProdDescription(String prodDescription) {
    this.prodDescription = prodDescription;
    return this;
  }

  public SewingPattern withSuggestedFabric(String suggestedFabric) {
    this.suggestedFabric = suggestedFabric;
    return this;
  }


  public SewingPattern withRecommendedNotions(String recommendedNotions) {
    this.recommendedNotions = recommendedNotions;
    return this;
  }

  public SewingPattern withLinksToPhotos(List<String> linksToPhotos) {
    this.linksToPhotos = linksToPhotos;
    return this;
  }

  public SewingPattern withNamesOfPhotos(List<String> namesOfPhotos) {
    this.namesOfPhotos = namesOfPhotos;
    return this;
  }

  public SewingPattern withPhoto(File photo) {
    this.photo = photo;
    return this;
  }


  public int getId() {
    return id;
  }

  public String getProdNumber() {
    return prodNumber;
  }

  public String getBrand() {
    return brand;
  }

  public String getTitle() {
    return title;
  }

  public String getSize() {
    return size;
  }


  public List<String> getClothingType() {
    return clothingType;
  }


  public String getProdDescription() {
    return prodDescription;
  }


  public String getSuggestedFabric() {
    return suggestedFabric;
  }


  public String getRecommendedNotions() {
    return recommendedNotions;
  }

  public List<String> getLinksToPhotos() {
    return linksToPhotos;
  }

  public List<String> getNamesOfPhotos() {
    return namesOfPhotos;
  }

  public File getPhoto() {
    return photo;
  }


}