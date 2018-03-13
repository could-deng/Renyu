package could.bluepay.renyumvvm.widget.pulltorefresh;

public enum SwipeRefreshDirection {

    TOP(0),
    BOTTOM(1),
    BOTH(2);

    private int mValue;

    SwipeRefreshDirection(int value) {
        this.mValue = value;
    }

    public static SwipeRefreshDirection getFromInt(int value) {
        for (SwipeRefreshDirection direction : SwipeRefreshDirection.values()) {
            if (direction.mValue == value) {
                return direction;
            }
        }
        return BOTH;
    }

}
