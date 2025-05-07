public enum Risk_Cat {
    Low(1),
    Medium(2),
    High(3),
    very_high(4);
    int score;
    Risk_Cat(int score) {
        this.score = score;
    }
    int getScore() {
        return score;
    }

}
