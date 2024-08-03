package info.journeymap.shaded.ar.com.hjg.pngj;

class RowInfo {

    public final ImageInfo imgInfo;

    public final Deinterlacer deinterlacer;

    public final boolean imode;

    int dY;

    int dX;

    int oY;

    int oX;

    int rowNseq;

    int rowNreal;

    int rowNsubImg;

    int rowsSubImg;

    int colsSubImg;

    int bytesRow;

    int pass;

    byte[] buf;

    int buflen;

    public RowInfo(ImageInfo imgInfo, Deinterlacer deinterlacer) {
        this.imgInfo = imgInfo;
        this.deinterlacer = deinterlacer;
        this.imode = deinterlacer != null;
    }

    void update(int rowseq) {
        this.rowNseq = rowseq;
        if (this.imode) {
            this.pass = this.deinterlacer.getPass();
            this.dX = this.deinterlacer.dX;
            this.dY = this.deinterlacer.dY;
            this.oX = this.deinterlacer.oX;
            this.oY = this.deinterlacer.oY;
            this.rowNreal = this.deinterlacer.getCurrRowReal();
            this.rowNsubImg = this.deinterlacer.getCurrRowSubimg();
            this.rowsSubImg = this.deinterlacer.getRows();
            this.colsSubImg = this.deinterlacer.getCols();
            this.bytesRow = (this.imgInfo.bitspPixel * this.colsSubImg + 7) / 8;
        } else {
            this.pass = 1;
            this.dX = this.dY = 1;
            this.oX = this.oY = 0;
            this.rowNreal = this.rowNsubImg = rowseq;
            this.rowsSubImg = this.imgInfo.rows;
            this.colsSubImg = this.imgInfo.cols;
            this.bytesRow = this.imgInfo.bytesPerRow;
        }
    }

    void updateBuf(byte[] buf, int buflen) {
        this.buf = buf;
        this.buflen = buflen;
    }
}