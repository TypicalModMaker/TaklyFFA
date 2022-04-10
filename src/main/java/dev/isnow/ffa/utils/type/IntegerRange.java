package dev.isnow.ffa.utils.type;

import java.util.Map;

public final class IntegerRange
{
    private final int minRange;
    private final int maxRange;

    public IntegerRange(final int minRange, final int maxRange) {
        this.minRange = minRange;
        this.maxRange = maxRange;
    }

    public int getMinRange() {
        return this.minRange;
    }

    public int getMaxRange() {
        return this.maxRange;
    }

    public static <V> Option<V> inRange(final int value, final Map<IntegerRange, V> rangeMap) {
        for (final Map.Entry<IntegerRange, V> entry : rangeMap.entrySet()) {
            final IntegerRange range = entry.getKey();
            if (value >= range.getMinRange() && value <= range.getMaxRange()) {
                return Option.of(entry.getValue());
            }
        }
        return Option.none();
    }

}
