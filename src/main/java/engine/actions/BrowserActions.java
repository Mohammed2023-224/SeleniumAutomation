package engine.actions;

import engine.exceptions.CustomExceptions;
import engine.reporters.Loggers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class BrowserActions {
    private BrowserActions() {
    }

    public static void navigateTo(WebDriver driver, String url) {
        try {
            driver.navigate().to(url);
            Loggers.logInfo("Navigated to url: " + url);
        } catch (Exception e) {
            Loggers.logError("Failed when navigating to page: " + url);
            throw e;
        }
    }

    public static void navigateBack(WebDriver driver) {
        try {
            driver.navigate().back();
            Loggers.logInfo("Navigated back to last page");
        } catch (Exception e) {
            Loggers.logError("Failed when navigating back");
            throw e;
        }
    }

    public static void navigateForward(WebDriver driver) {
        try {
            driver.navigate().forward();
            Loggers.logInfo("Navigated forward");
        } catch (Exception e) {
            Loggers.logError("Failed when navigating forward");
            throw e;
        }
    }

    public static void switchIframe(WebDriver driver, By frameLocator) {
        try {
            driver.switchTo().frame(driver.findElement(frameLocator));
            Loggers.logInfo("Switch to iframe located at: " + frameLocator);
        } catch (Exception e) {
            Loggers.logError("Failed when switching to iframe: " + frameLocator);
            throw e;
        }
    }

    public static void switchParentFrame(WebDriver driver) {
        try {
            driver.switchTo().parentFrame();
            Loggers.logInfo("Switch to parent frame");
        } catch (Exception e) {
            Loggers.logError("Failed switching to parent frame");
            throw e;
        }
    }

    public static void switchDefaultFrame(WebDriver driver) {
        try {
            driver.switchTo().defaultContent();
            Loggers.logInfo("Switch to default frame");
        } catch (Exception e) {
            Loggers.logError("Failed switching to default frame");
            throw e;
        }
    }

    public static void switchToWindowByIndex(WebDriver driver, int windowNumber) {
        int windowNumbers;
        try {
            ArrayList<String> windows = new ArrayList<>(driver.getWindowHandles());
            windowNumbers = windows.size();
            if (windowNumber >= 0 && windowNumber < windowNumbers) {
                driver.switchTo().window(windows.get(windowNumber));
                Loggers.logInfo("Switch to windows number: " + windowNumber);
            } else {
                Loggers.logWarn("Current number of windows: " + windowNumbers);
            }
        } catch (Exception e) {
            Loggers.logError("Failed switching to windows number: " + windowNumber);
            throw e;
        }
    }

    public static void switchToWindowByTitle(WebDriver driver, String title) {
        ArrayList<String> windows = new ArrayList<>(driver.getWindowHandles());
        if (windows.size() <= 1) {
            Loggers.logError("Couldn't find more that the current window");
            return;
        }
        for (String t : windows) {
            try {
                driver.switchTo().window(t);
                String currentTitle = driver.getTitle();
                if (currentTitle != null && currentTitle.equalsIgnoreCase(title)) {
                    Loggers.logInfo("Switch to windows titled: " + title);
                    return;
                }
            } catch (Exception e) {
                Loggers.logWarn("Couldn't inspect window: " + e.getMessage());
            }
        }
        throw new CustomExceptions("No windows was found with title " + title);
    }

    public static void switchToWindowByURL(WebDriver driver, String url) {
        ArrayList<String> windows = new ArrayList<>(driver.getWindowHandles());
        if (windows.size() <= 1) {
            Loggers.logError("Couldn't find more that the current window");
            return;
        }
        for (String u : windows) {
            try {
                driver.switchTo().window(u);
                String currentUrl = driver.getCurrentUrl();
                if (currentUrl != null && currentUrl.equals(url)) {
                    Loggers.logInfo("Switch to windows with url: " + url);
                    return;
                }
            } catch (Exception e) {
                Loggers.logError("Current window doesn't has the url: " + url);
            }
        }
        throw new CustomExceptions("No windows was found with url " + url);

    }

    public static void switchToFirstWindow(WebDriver driver) {
        try {
            ArrayList<String> windows = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(windows.getFirst());
            Loggers.logInfo("Switch to main window");
        } catch (Exception e) {
            Loggers.logError("Failed to switch to the first window");
            throw e;
        }
    }

    public static void acceptAlert(WebDriver driver) {
        try {
            driver.switchTo().alert().accept();
            Loggers.logInfo("Accept existing alert");
        } catch (Exception e) {
            Loggers.logError("Couldn't accept the alert");
            throw e;
        }
    }

    public static void startNewTab(WebDriver driver) {
        if (driver == null) return;
        JSActions.executeScript(driver, "window.open();");
        List<String> handles = new ArrayList<>(driver.getWindowHandles());
        String validTab = null;
        for (String handle : handles) {
            try {
                driver.switchTo().window(handle);
                String url = driver.getCurrentUrl();
                if (url == null) throw new IllegalStateException("The current url is null");
                boolean isBlank = url.equals("about:blank");
                boolean isInternalPage = !isBlank && (url.startsWith("edge://") || url.startsWith("chrome://") || url.startsWith("about:"));
                if (isBlank) {
                    validTab = handle;
                } else if (isInternalPage) {
                    Loggers.logInfo("edge download tab can't be closed");
                } else {
                    driver.close();
                }
            } catch (Exception e) {
                Loggers.logInfo("Error handling tab: " + e.getMessage());
            }
        }
        if (validTab == null) throw new IllegalStateException("The new valid tab is null");
        try {
            driver.switchTo().window(validTab);
        } catch (Exception e) {
            Loggers.logError("Failed to switch to the valid new tab");
            throw e;
        }
    }
}