package jp.nat.crypto.Models;


import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@ToString
@EqualsAndHashCode
public class Cryptocurrency {
    @Getter @Setter
    private Long id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String symbol;
    @Setter
    private Double costCurrentUSD;
    @Setter
    private Double costCurrentRUB;
    @Setter
    private String deltaDayPercentage;
    @Setter
    private String deltaWeekPercentage;
    @Setter
    private String tradingVolumeDay;


    public Double getCostCurrentUSD() {
        return round(costCurrentUSD).doubleValue();
    }

    public Double getCostCurrentRUB() {
        return round(costCurrentRUB).doubleValue();
    }

    public String getDeltaDayPercentage() {
        String delta = round(Double.parseDouble(deltaDayPercentage)).toString();
        if (!delta.startsWith("-")) {
            delta = "+" + delta;
        }
        return delta;
    }

    public String getDeltaWeekPercentage() {
        String delta = round(Double.parseDouble(deltaWeekPercentage)).toString();
        if (!delta.startsWith("-")) {
            delta = "+" + delta;
        }
        return delta;
    }

    public String getTradingVolumeDay() {
        return round(Double.parseDouble(tradingVolumeDay)).toPlainString();
    }



    private static BigDecimal round(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd;
    }
}
