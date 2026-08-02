package com.kenneth.nextrole.Tools;
import com.kenneth.nextrole.exception.JobParseException;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;


public class PlaywrightExtractor implements JobExtractor {

    @Override
    public String extract(String url) {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            BrowserContext context = browser.newContext()) { //placed these in try block because I don't want to write extra code to manually close these
            try (browser) {
                Page page = context.newPage();
                page.navigate(url);
                try {
                    page.waitForLoadState(LoadState.NETWORKIDLE,
                            new Page.WaitForLoadStateOptions().setTimeout(5000));
                } catch (TimeoutError e) {
                    // page never went idle (chat widget, polling, etc.) — proceed anyway
                }
                return page.locator("body").innerText();
            }
        } catch (PlaywrightException e) {
            throw new JobParseException("Error retrieving website text from " + url);
        }
    }
}
