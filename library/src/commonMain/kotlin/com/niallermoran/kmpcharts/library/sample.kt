package com.niallermoran.kmpcharts.library

object Sample{

    val irelandMonthlyTemperatureData = listOf(
        ChartDataPoint(
            xValue = 1.0,
            yValue = 5.2,
            summary = "January is Ireland's coldest month with frequent rain and occasional frost. Days are short with limited daylight, but temperatures rarely drop below freezing along the coast."
        ),
        ChartDataPoint(
            xValue = 2.0,
            yValue = 5.8,
            summary = "February remains cold with continuing winter weather patterns. Snow is possible but uncommon, especially in coastal areas. The days gradually begin to lengthen."
        ),
        ChartDataPoint(
            xValue = 3.0,
            yValue = 7.1,
            summary = "March marks the beginning of spring with milder temperatures and longer days. Rain is still frequent, but daffodils bloom and the countryside begins to green up."
        ),
        ChartDataPoint(
            xValue = 4.0,
            yValue = 9.3,
            summary = "April brings pleasant spring weather with warming temperatures and beautiful blooming flowers. Showers are common, giving rise to the saying 'April showers bring May flowers.'"
        ),
        ChartDataPoint(
            xValue = 5.0,
            yValue = 12.1,
            summary = "May is one of Ireland's most beautiful months with mild temperatures and lush green landscapes. Rainfall decreases slightly, making it excellent for outdoor activities and tourism."
        ),
        ChartDataPoint(
            xValue = 6.0,
            yValue = 14.8,
            summary = "June offers warm, pleasant weather with the longest days of the year approaching. The countryside is at its most vibrant green, though occasional showers keep everything fresh."
        ),
        ChartDataPoint(
            xValue = 7.0,
            yValue = 16.4,
            summary = "July is typically Ireland's warmest month with comfortable temperatures and extended daylight hours. Perfect for festivals, outdoor events, and exploring the scenic coastline."
        ),
        ChartDataPoint(
            xValue = 8.0,
            yValue = 16.1,
            summary = "August remains warm with temperatures similar to July. It's peak tourist season with generally favorable weather, though the occasional Atlantic storm can bring heavy rain."
        ),
        ChartDataPoint(
            xValue = 9.0,
            yValue = 13.9,
            summary = "September sees the transition to autumn with cooling temperatures but often pleasant, mild weather. Harvest season brings beautiful golden colors to the countryside."
        ),
        ChartDataPoint(
            xValue = 10.0,
            yValue = 10.8,
            summary = "October brings crisp autumn weather with falling leaves and shorter days. Rain becomes more frequent as Atlantic storm systems begin to dominate the weather patterns."
        ),
        ChartDataPoint(
            xValue = 11.0,
            yValue = 7.6,
            summary = "November is characterized by cool, wet weather as winter approaches. Frequent rain and strong winds from Atlantic storms make it one of the wettest months."
        ),
        ChartDataPoint(
            xValue = 12.0,
            yValue = 6.1,
            summary = "December brings cold, dark days with the shortest daylight hours of the year. Rain is frequent, but snow is relatively rare except in higher elevations inland."
        )
    )

    val bitcoinWeekly2024 = listOf(
        ChartDataPoint(
            xValue = 1704067200.0, // January 1, 2024 00:00:00 UTC
            yValue = 42285.50,
            summary = "Bitcoin started 2024 with optimism as ETF approval hopes drove institutional interest and price recovery from December lows."
        ),
        ChartDataPoint(
            xValue = 1704672000.0, // January 8, 2024 00:00:00 UTC
            yValue = 46150.75,
            summary = "Strong momentum continued as Bitcoin ETF approval anticipation intensified, pushing prices higher in the first full week of 2024."
        ),
        ChartDataPoint(
            xValue = 1705276800.0, // January 15, 2024 00:00:00 UTC
            yValue = 42890.25,
            summary = "Some profit-taking and consolidation occurred as markets awaited final regulatory decisions on spot Bitcoin ETF applications."
        ),
        ChartDataPoint(
            xValue = 1705881600.0, // January 22, 2024 00:00:00 UTC
            yValue = 39523.63,
            summary = "Bitcoin hit its yearly low amid broader market uncertainty and regulatory concerns before the historic ETF approvals."
        ),
        ChartDataPoint(
            xValue = 1706486400.0, // January 29, 2024 00:00:00 UTC
            yValue = 43250.80,
            summary = "Recovery began as positive signals emerged regarding ETF approvals, with institutional buyers showing renewed interest."
        ),
        ChartDataPoint(
            xValue = 1707091200.0, // February 5, 2024 00:00:00 UTC
            yValue = 47150.90,
            summary = "Bitcoin surged on growing confidence about imminent ETF approvals and increasing institutional adoption momentum."
        ),
        ChartDataPoint(
            xValue = 1707696000.0, // February 12, 2024 00:00:00 UTC
            yValue = 50850.25,
            summary = "Strong upward momentum continued as the first Bitcoin ETFs began trading, attracting significant institutional capital inflows."
        ),
        ChartDataPoint(
            xValue = 1708300800.0, // February 19, 2024 00:00:00 UTC
            yValue = 52750.45,
            summary = "Bitcoin maintained strength as ETF trading volumes exceeded expectations and corporate adoption announcements increased."
        ),
        ChartDataPoint(
            xValue = 1708905600.0, // February 26, 2024 00:00:00 UTC
            yValue = 57240.70,
            summary = "Late February saw continued appreciation as institutional demand through ETFs remained robust and supply dynamics tightened."
        ),
        ChartDataPoint(
            xValue = 1709510400.0, // March 4, 2024 00:00:00 UTC
            yValue = 62890.15,
            summary = "March opened with strong gains as Bitcoin approached previous all-time highs, driven by sustained ETF inflows and institutional interest."
        ),
        ChartDataPoint(
            xValue = 1710115200.0, // March 11, 2024 00:00:00 UTC
            yValue = 70250.80,
            summary = "Bitcoin broke through previous resistance levels and set new all-time highs as mainstream adoption accelerated significantly."
        ),
        ChartDataPoint(
            xValue = 1710720000.0, // March 18, 2024 00:00:00 UTC
            yValue = 73100.25,
            summary = "Historic week as Bitcoin reached its all-time high above $73,000, marking a watershed moment for cryptocurrency mainstream acceptance."
        ),
        ChartDataPoint(
            xValue = 1711324800.0, // March 25, 2024 00:00:00 UTC
            yValue = 69850.60,
            summary = "Some consolidation after reaching peak levels, with profit-taking and healthy correction following the euphoric highs."
        ),
        ChartDataPoint(
            xValue = 1711929600.0, // April 1, 2024 00:00:00 UTC
            yValue = 66750.40,
            summary = "April began with continued consolidation as markets digested the massive gains and institutional flows stabilized."
        ),
        ChartDataPoint(
            xValue = 1712534400.0, // April 8, 2024 00:00:00 UTC
            yValue = 71200.90,
            summary = "Bitcoin found renewed strength in early April as the halving event approached, historically a bullish catalyst for prices."
        ),
        ChartDataPoint(
            xValue = 1713139200.0, // April 15, 2024 00:00:00 UTC
            yValue = 67890.75,
            summary = "Pre-halving volatility emerged as traders positioned for the supply reduction event scheduled for later in April."
        ),
        ChartDataPoint(
            xValue = 1713744000.0, // April 22, 2024 00:00:00 UTC
            yValue = 64250.30,
            summary = "Bitcoin halving occurred, reducing block rewards and future supply, though immediate price impact was muted by existing momentum."
        ),
        ChartDataPoint(
            xValue = 1714348800.0, // April 29, 2024 00:00:00 UTC
            yValue = 63890.85,
            summary = "Post-halving stabilization as markets adjusted to the reduced supply schedule and continued institutional accumulation."
        ),
        ChartDataPoint(
            xValue = 1714953600.0, // May 6, 2024 00:00:00 UTC
            yValue = 61750.20,
            summary = "May opened with some weakness as macroeconomic concerns and profit-taking pressured prices following the halving event."
        ),
        ChartDataPoint(
            xValue = 1715558400.0, // May 13, 2024 00:00:00 UTC
            yValue = 59420.65,
            summary = "Continued softness as investors reassessed positions amid changing interest rate expectations and global economic uncertainty."
        ),
        ChartDataPoint(
            xValue = 1716163200.0, // May 20, 2024 00:00:00 UTC
            yValue = 67850.40,
            summary = "Strong recovery as institutional buying resumed and positive regulatory developments in multiple jurisdictions emerged."
        ),
        ChartDataPoint(
            xValue = 1716768000.0, // May 27, 2024 00:00:00 UTC
            yValue = 68950.75,
            summary = "Late May strength continued as Memorial Day weekend saw sustained institutional interest and reduced selling pressure."
        ),
        ChartDataPoint(
            xValue = 1717372800.0, // June 3, 2024 00:00:00 UTC
            yValue = 71250.90,
            summary = "June opened strongly as post-halving supply dynamics began showing effects and institutional adoption continued expanding."
        ),
        ChartDataPoint(
            xValue = 1717977600.0, // June 10, 2024 00:00:00 UTC
            yValue = 69450.30,
            summary = "Mid-June consolidation as markets balanced between continued institutional demand and natural profit-taking activities."
        ),
        ChartDataPoint(
            xValue = 1718582400.0, // June 17, 2024 00:00:00 UTC
            yValue = 66750.85,
            summary = "Some pullback as Federal Reserve policy signals created uncertainty about future monetary conditions and risk asset appetite."
        ),
        ChartDataPoint(
            xValue = 1719187200.0, // June 24, 2024 00:00:00 UTC
            yValue = 64200.15,
            summary = "Late June weakness as summer trading patterns emerged and some institutional rebalancing occurred ahead of Q2 end."
        ),
        ChartDataPoint(
            xValue = 1719792000.0, // July 1, 2024 00:00:00 UTC
            yValue = 63150.60,
            summary = "July began with continued softness as Q2 ended and some profit-taking occurred before summer holiday periods."
        ),
        ChartDataPoint(
            xValue = 1720396800.0, // July 8, 2024 00:00:00 UTC
            yValue = 58420.25,
            summary = "Mid-summer correction continued as trading volumes decreased and some long-term holders took profits at elevated levels."
        ),
        ChartDataPoint(
            xValue = 1721001600.0, // July 15, 2024 00:00:00 UTC
            yValue = 60750.80,
            summary = "Modest recovery as support levels held and bargain hunters emerged during the traditional summer trading lull."
        ),
        ChartDataPoint(
            xValue = 1721606400.0, // July 22, 2024 00:00:00 UTC
            yValue = 67850.45,
            summary = "Strong late July rally as renewed institutional interest and positive regulatory news drove prices higher again."
        ),
        ChartDataPoint(
            xValue = 1722211200.0, // July 29, 2024 00:00:00 UTC
            yValue = 70250.30,
            summary = "July ended strongly as month-end rebalancing and continued ETF inflows supported prices near previous highs."
        ),
        ChartDataPoint(
            xValue = 1722816000.0, // August 5, 2024 00:00:00 UTC
            yValue = 58950.75,
            summary = "August opened with volatility as global market turbulence and carry trade unwinding affected risk assets including Bitcoin."
        ),
        ChartDataPoint(
            xValue = 1723420800.0, // August 12, 2024 00:00:00 UTC
            yValue = 60180.90,
            summary = "Recovery began as markets stabilized following the early August volatility and institutional buying resumed gradually."
        ),
        ChartDataPoint(
            xValue = 1724025600.0, // August 19, 2024 00:00:00 UTC
            yValue = 59750.20,
            summary = "Continued consolidation in a tight range as markets awaited Federal Reserve policy signals and macroeconomic clarity."
        ),
        ChartDataPoint(
            xValue = 1724630400.0, // August 26, 2024 00:00:00 UTC
            yValue = 64200.65,
            summary = "Late August strength as Jackson Hole symposium provided clearer Fed policy direction and risk appetite improved."
        ),
        ChartDataPoint(
            xValue = 1725235200.0, // September 2, 2024 00:00:00 UTC
            yValue = 59850.40,
            summary = "September opened with some weakness as summer ended and institutional activity resumed with mixed sentiment."
        ),
        ChartDataPoint(
            xValue = 1725840000.0, // September 9, 2024 00:00:00 UTC
            yValue = 55420.85,
            summary = "Mid-September decline as broader market concerns and profit-taking pressured Bitcoin below key support levels."
        ),
        ChartDataPoint(
            xValue = 1726444800.0, // September 16, 2024 00:00:00 UTC
            yValue = 63150.25,
            summary = "Strong recovery as Federal Reserve rate cut expectations increased and institutional sentiment improved markedly."
        ),
        ChartDataPoint(
            xValue = 1727049600.0, // September 23, 2024 00:00:00 UTC
            yValue = 65750.70,
            summary = "Continued strength as the Fed delivered expected rate cuts and risk-on sentiment returned to financial markets."
        ),
        ChartDataPoint(
            xValue = 1727654400.0, // September 30, 2024 00:00:00 UTC
            yValue = 63890.15,
            summary = "Quarter-end consolidation as Q3 closed with mixed performance but overall positive sentiment for Q4 outlook."
        ),
        ChartDataPoint(
            xValue = 1728259200.0, // October 7, 2024 00:00:00 UTC
            yValue = 62450.90,
            summary = "October began with cautious optimism as Q4 historically strong season approached and institutional interest remained solid."
        ),
        ChartDataPoint(
            xValue = 1728864000.0, // October 14, 2024 00:00:00 UTC
            yValue = 67250.30,
            summary = "Mid-October rally as traditional Q4 strength began emerging and ETF flows remained consistently positive."
        ),
        ChartDataPoint(
            xValue = 1729468800.0, // October 21, 2024 00:00:00 UTC
            yValue = 69850.75,
            summary = "Strong late October performance as Bitcoin approached previous highs with renewed institutional momentum building."
        ),
        ChartDataPoint(
            xValue = 1730073600.0, // October 28, 2024 00:00:00 UTC
            yValue = 72100.20,
            summary = "Halloween week surge as Bitcoin reached new yearly highs driven by election uncertainty and safe-haven demand."
        ),
        ChartDataPoint(
            xValue = 1730678400.0, // November 4, 2024 00:00:00 UTC
            yValue = 69450.85,
            summary = "Election week volatility as markets positioned for potential policy changes and regulatory shifts under new administration."
        ),
        ChartDataPoint(
            xValue = 1731283200.0, // November 11, 2024 00:00:00 UTC
            yValue = 81250.40,
            summary = "Post-election surge as pro-crypto policy expectations drove massive institutional inflows and retail FOMO buying."
        ),
        ChartDataPoint(
            xValue = 1731888000.0, // November 18, 2024 00:00:00 UTC
            yValue = 91750.90,
            summary = "Euphoric rally continued as Trump victory sparked hopes for strategic Bitcoin reserves and crypto-friendly regulations."
        ),
        ChartDataPoint(
            xValue = 1732492800.0, // November 25, 2024 00:00:00 UTC
            yValue = 98850.25,
            summary = "Thanksgiving week saw Bitcoin approaching $100K milestone as institutional adoption accelerated and FOMO intensified globally."
        ),
        ChartDataPoint(
            xValue = 1733097600.0, // December 2, 2024 00:00:00 UTC
            yValue = 96750.70,
            summary = "December opened with some consolidation near $100K as profit-taking emerged but underlying demand remained extremely strong."
        ),
        ChartDataPoint(
            xValue = 1733702400.0, // December 9, 2024 00:00:00 UTC
            yValue = 99420.15,
            summary = "Push toward psychological $100K level intensified as year-end institutional rebalancing and FOMO buying accelerated."
        ),
        ChartDataPoint(
            xValue = 1734307200.0, // December 16, 2024 00:00:00 UTC
            yValue = 106850.80,
            summary = "Historic breakthrough above $100K as Bitcoin achieved a major psychological milestone with massive global media attention."
        ),
        ChartDataPoint(
            xValue = 1734912000.0, // December 23, 2024 00:00:00 UTC
            yValue = 95750.45,
            summary = "Christmas week profit-taking and low volume caused temporary pullback from six-figure levels as year-end approached."
        ),
        ChartDataPoint(
            xValue = 1735516800.0, // December 30, 2024 00:00:00 UTC
            yValue = 94250.90,
            summary = "Year ended with consolidation below $100K as investors took profits, but 2024 marked Bitcoin's transformation into mainstream asset."
        )
    )

}