package com.functions;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.TimerTrigger;

public class MenuScraper {

    @FunctionName("ScrapeMenu")
    public void run(
        @TimerTrigger(
            name = "scraper trigger",
            schedule = "0 * * * * *") final ExecutionContext context) {
        context.getLogger().info("Starting scraping.");

        context.getLogger().info("Scraping complete.");
    }
}
