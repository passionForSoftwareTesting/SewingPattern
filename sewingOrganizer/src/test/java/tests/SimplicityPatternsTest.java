package tests;


import model.SewingPattern;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;

public class SimplicityPatternsTest {
  /*
   * 1. Open Simplicity site;
   * 2. Input pattern number from DataProvider to the search field; Press enter;
   * 3. Search result page opens:
   *    3.1 One page with results opens; -> take all Data from that page and save all Images from pattern cover;
   *    3.2 Page opens with multiple results; -> press Vew all icon to show all finding results in one page;
   *    3.3 (in plans) Page opens with No results found; -> goTo Google Search and try to find page with pattern from PatternReview website;
   */

  private WebDriver driver;
  private String baseUrl, baseGoogleUrl;
  private WebElement element;
  private String defaultBrand = "Simplicity";


  @DataProvider(name = "test1")
  public Object[] createData1() {
    Object[][] objects1 = {
            {1108},
            {1064},
            {1165},
            {1277},
            {1370},
            {1425},
            {1462},
            {1463},
            {1469},
            {1544},
            {1586},
            {1610},
            {1653},
            {1716},
            {1757},
            {1800},
            {1887},
            {2247},
            {2058}
    };
    return objects1;
  }

  @DataProvider(name = "test2")
  public Object[] createData2() {
    Object[][] objects2 =
            {
                    {8014},
                    {8022},
                    {8056},
                    {8059},
                    {8061},
                    {8172},
                    {8178},
                    {8212},
                    {8219},
                    {8228},
                    {8229},
                    {8337},
                    {8339},
                    {8375},
                    {8376},
                    {8377},
                    {8378},
                    {8415},
                    {8512},
                    {8516},
                    {8543},
                    {8544},
                    {8545},
                    {8546},
                    {8548},
                    {8551},
                    {8556},
                    {8560},
                    {8561},
                    {8604},
                    {8608},
                    {8637},
                    {8658},
                    {8688},
                    {8689},
                    {8697},
                    {8699},
                    {8700},
                    {8704},
                    {8734},
                    {8735},
                    {8743},
                    {8744},
                    {8746},
                    {8751},
                    {8752},
                    {8753},
                    {8560},
                    {8561},
                    {8736},
                    {8740},
                    {8742},
                    {8787},
                    {8790},
                    {8791},
                    {8792},
                    {8793},
                    {8794},
                    {8795},
                    {8803},
                    {8844},
                    {8848},
                    {8859},
                    {8871},
                    {8879},
                    {8883},
                    {8891},
                    {8920},
                    {8921},
                    {8948},
                    {8949},
                    {8956},
                    {8957},
                    {8981},
                    {8982},
                    {8983},
                    {8984},
                    {8985},
                    {8988},
                    {8989},
                    {8992},
                    {8993},
                    {9006},
                    {9011},
                    {9013},
                    {9014},
                    {9017},
                    {9018},
                    {9041},
                    {9045},
                    {9051},
                    {9052},
                    {9059}

            };

    return objects2;
  }

  @DataProvider(name = "noPatternFoundInSimplicityCom")
  public Object[] createData3() {
    Object[][] objects3 =
            { //patterns, that have no match in Simplicity site
                    {1462},
                    {1469},
                    {1610},
                    {1716},
                    {1757}, //multiple results with No match at all
                    {8022},
                    {8061},
                    {8339},
                    {8544},
                    {8752} //search doesn't contain pattern that matched
            };

    return objects3;
  }

  @DataProvider(name = "multipleResultsWithNoMatchToPatternId")
  public Object[] createData4() {
    Object[][] objects4 =
            {
                    {1757}, //multiple results with No match at all
                    {1544}, //hmultiple, but has matching result
            };

    return objects4;
  }

  @BeforeMethod
  public void setUp() throws Exception {
    System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/macos/chromedriver");
    driver = new ChromeDriver();
    baseUrl = "https://www.simplicity.com/";
    baseGoogleUrl = "http://www.google.com";
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
  }

  @Test(dataProvider = "test1")

  public void testSearchingResults(Integer patternId) throws Exception {
    String brand = "Simplicity";
    String idString = Integer.toString(patternId);
    openPage(baseUrl);
    getRidOfPopUp();
    searchResultInBaseSite(idString);
    new WebDriverWait(driver, 5);
    if (!driver.findElements(By.className("prod-details__prod-number")).isEmpty()) {
      assertTrue(isTitleContainsPatterId(driver.findElement(By.cssSelector("h1.prod-details__title")).getText(), patternId), "Search results doesn't contain pattern " + patternId);
      assertFalse(newSewingPatternObject(patternId, brand).getProdDescription().isEmpty());
      savingPictures(brand, idString, patternId);
    } else {
      if (!driver.findElements(By.cssSelector("p.search-result-for__subtitle--empty")).isEmpty()) {
        System.out.println("patternId = " + patternId + ": " + driver.findElement(By.cssSelector("p.search-result-for__subtitle--empty")).getText());
        fail();
      } else {
        System.out.println("patternId = " + patternId + " We found more than one result for your search.");
        if (!driver.findElements(By.cssSelector("div.slider-product__wrapper div.slider-product__slide")).isEmpty()) {
          viewAllElementInSearchResults(patternId);//make sure, that all elements in search result are displayed in the page
          openPage(moreThanOneItemInSearch(patternId)); //open right page with patternId from the search list results
          assertFalse(newSewingPatternObject(patternId, brand).getProdDescription().isEmpty()); //making sure, that Description of the pattern doesn't empty;
          savingPictures(brand, idString, patternId);
        }
      }
    }
  }




  private boolean isTitleContainsPatterId(String title, int patternId) {
    boolean titleContainsPatternId = false;
    if ((title.contains("Simplicity")) && (((title.contains(" " + patternId + " ")) || (title.contains("S" + patternId))))) {
      titleContainsPatternId = true;
    }
    return titleContainsPatternId;
  }

  private void getRidOfPopUp() {
    WebElement popUP = driver.findElement(By.className("sign-up-popup__close"));
    if (popUP.isDisplayed()) {
      System.out.println("popUp is displayed");
      popUP.click();
      new WebDriverWait(driver, 5);
    }
  }

  private void savingPictures(String brand, String idString, int patternId) throws IOException {
    //newSewingPatternObject(patternId, brand);
    ArrayList<String> imgUrl = getImages();
    ArrayList<String> imgNames = new ArrayList<>();
    System.out.println("Names to images: ");

    for (String e : imgUrl) {
      imgNames.add(e);
      System.out.println(imageNames(e));
    }

    String path = "src/test/resources/SewingProject/" + patternId;
    createFolders(path, idString);
    System.out.println("path = " + path);
    //reading url and retrieve an image
    for (String urlString : imgUrl) {
      downloadImage(urlString, path);
    }
  }

  private void viewAllElementInSearchResults(int patternId) {
    WebElement viewAllElement = driver.findElement(By.cssSelector("a.tools-snippet__right"));
    System.out.println("is View All element present? " + viewAllElement.isDisplayed());
    if (viewAllElement.isDisplayed()) {
      String linkSearchInTheCatalog = viewAllElement.getAttribute("href");
      System.out.println("link to the search in Catalog: \n" + linkSearchInTheCatalog);
      System.out.println(driver.getCurrentUrl());
      viewAllElement.click();
      System.out.println(driver.getCurrentUrl());
      //wait until visibility of the element "span.pager__result"
      waitUntilElementDisplayed(By.cssSelector("span.pager__result"));
      assertEquals(driver.getCurrentUrl(), linkSearchInTheCatalog);
    }
  }

  private void waitUntilElementDisplayed(By locator) {
    while (!driver.findElement(locator).isDisplayed()) {
      new WebDriverWait(driver, 0);
    }
  }

  private String moreThanOneItemInSearch(int patternId) {
    String patternLink = "no link";
    WebElement productCardBlock = driver.findElement(By.cssSelector("div.product-card-block"));
    List<WebElement> productBlocks = driver.findElements(By.cssSelector("div.product-card__img-box a"));
    List<WebElement> titleProducts = driver.findElements(By.className("product-details__tile-link"));
    assertFalse(productBlocks.isEmpty());
    ArrayList<String> linkProductBlock = new ArrayList<>();
    ArrayList<String> titleProductBlock = new ArrayList<>();
    for (WebElement el1 : productBlocks) {
      linkProductBlock.add(el1.getAttribute("href"));
    }
    boolean titleContainsPatterId = false;
    for (WebElement el2 : titleProducts) {
      titleProductBlock.add(el2.getText());
      if ((el2.getText().contains("Simplicity")) && (((el2.getText().contains(" " + patternId + " ")) || (el2.getText().contains("S" + patternId))))) {
        titleContainsPatterId = true;
        int i = titleProductBlock.size();
        patternLink = linkProductBlock.get(i - 1);
        String patternTitle = titleProductBlock.get(i - 1);
        System.out.println("patternTitle = " + patternTitle);
      }
    }
    assertTrue(titleContainsPatterId);
    System.out.println("patternLink = " + patternLink);
    return patternLink;
  }

  public void downloadImage(String urlString, String folderName) throws MalformedURLException, IOException {
    URL url = new URL(urlString);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestProperty(
            "User-Agent",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Safari/537.36");

    BufferedImage image = ImageIO.read(connection.getInputStream());
    System.out.println("imgNames(urlString) = " + imageNames(urlString));
    String imgPath = folderName + "/" + imageNames(urlString);

    if (image != null) {
      //download image, save picture as pic.jpg to the folder imgPath
      ImageIO.write(image, "jpeg", new File(imgPath));

    }
  }

  public void searchResultInBaseSite(String str) {
    String locator = "search-q-1";
    WebElement element = driver.findElement(By.id(locator));
    element.sendKeys(str);
    element.sendKeys(Keys.RETURN);
  }

  public void searchResultInGoogle() {
    typeInGoogleSearch("Simplicity 8791");
    verifyResultsPage();
    openPage(getLink());
  }

  public SewingPattern newSewingPatternObject(int patternId, String brand) {
    SewingPattern sp = new SewingPattern();
    sp.withId(patternId).withBrand(brand)
            .withProdNumber(driver.findElement(By.className("prod-details__prod-number")).getText())
            // .withProdDescription(getTextFromList(By.cssSelector("div.tabs__container--description p")))
            .withProdDescription(getTextFromList(By.cssSelector("div.tabs__container--product  div.tabs__container--description")))
            .withTitle(driver.findElement(By.cssSelector("h1.prod-details__title")).getText());

    System.out.println("Pattern = " + sp.getId() +
            "\nBrand: " + sp.getBrand() +
            "\nTitle: " + sp.getTitle() +
            "\nProductNumber: " + sp.getProdNumber() +
            "\nProductDescription: " + sp.getProdDescription());
    return sp;
  }

  public ArrayList<String> getImages() {
    ArrayList<String> imgArrayList = new ArrayList<String>();
    driver.findElement(By.className("zoom-btn")).click();
    List<WebElement> webElementsImg = driver.findElements(By.cssSelector("div.zoom-popup__right img"));
    for (WebElement e : webElementsImg) {
      imgArrayList.add(concatLinks(e.getAttribute("src")));
    }

    System.out.println("Links to images: ");
    for (String e : imgArrayList) {
      System.out.println(e);
    }
    return imgArrayList;
  }

  public String concatLinks(String originalLink) {
    return originalLink.substring(0, originalLink.lastIndexOf("?width"));
  }

  public String imageNames(String link) {
    int i = link.lastIndexOf("simplicity");
    int j = link.length() - ".jpeg".length() + 1;
    return link.substring(i, j)
            .replaceAll("[/:.]", "").concat(".jpeg"); //two backslashes define one; one backslash - is an escape character, predefine in Java
  }

  public String getTextFromList(By locator) {
    StringBuilder collectingText = new StringBuilder();
    for (WebElement e : driver.findElements(locator)) {
      System.out.println("e.getText = " + e.getText());
      collectingText.append(e.getText());
    }
    return collectingText.toString();
  }

  public String getLink() {
    String source = "";
    String locator = "div.yuRUbf a";
    List<WebElement> resultElements = driver.findElements(By.cssSelector(locator));
    for (WebElement e : resultElements) {
      String link = e.getAttribute("href");
      if (link.startsWith("https://www.simplicity.com/")) {
        source = link;
      }
      System.out.println(link);
    }
    System.out.println("source = " + source);
    return source;
  }


  public void createFolders(String path, String patternId) {
    File dir = new File(path);
    if (!dir.exists()) {
      try {
        System.out.println("Folder " + patternId + " was created " + dir.mkdirs());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void openPage(String baseUrl) {
    driver.get(baseUrl);
  }

  private void typeInGoogleSearch(String query) {
    String locator = "input.gLFyf.gsfi";
    WebElement element = driver.findElement(By.cssSelector(locator));
    element.sendKeys(query);
    element.sendKeys(Keys.RETURN);
    List<WebElement> list = driver.findElements(By.cssSelector("div#search div.g"));
    System.out.println("Количество найденных элементов = " + list.size());
  }

  private void verifyResultsPage() {
    String resultsStatsElementId = "result-stats";
    WebElement element = driver.findElement(By.id(resultsStatsElementId));
    boolean isResultsDisplayed = element.isDisplayed();
    assertTrue(isResultsDisplayed);
    System.out.println("Find results: " + element.getText());
    System.out.println("Open windows: = " + driver.getWindowHandles().size());

  }

  @AfterMethod
  public void tearDown() throws Exception {
    driver.quit();
  }
}
