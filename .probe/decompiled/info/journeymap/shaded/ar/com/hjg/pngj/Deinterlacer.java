package info.journeymap.shaded.ar.com.hjg.pngj;

public class Deinterlacer {

    final ImageInfo imi;

    private int pass;

    private int rows;

    private int cols;

    int dY;

    int dX;

    int oY;

    int oX;

    int oXsamples;

    int dXsamples;

    private int currRowSubimg = -1;

    private int currRowReal = -1;

    private int currRowSeq;

    int totalRows = 0;

    private boolean ended = false;

    public Deinterlacer(ImageInfo iminfo) {
        this.imi = iminfo;
        this.pass = 0;
        this.currRowSeq = 0;
        this.setPass(1);
        this.setRow(0);
    }

    private void setRow(int n) {
        this.currRowSubimg = n;
        this.currRowReal = n * this.dY + this.oY;
        if (this.currRowReal < 0 || this.currRowReal >= this.imi.rows) {
            throw new PngjExceptionInternal("bad row - this should not happen");
        }
    }

    boolean nextRow() {
        this.currRowSeq++;
        if (this.rows != 0 && this.currRowSubimg < this.rows - 1) {
            this.setRow(this.currRowSubimg + 1);
        } else {
            if (this.pass == 7) {
                this.ended = true;
                return false;
            }
            this.setPass(this.pass + 1);
            if (this.rows == 0) {
                this.currRowSeq--;
                return this.nextRow();
            }
            this.setRow(0);
        }
        return true;
    }

    boolean isEnded() {
        return this.ended;
    }

    void setPass(int p) {
        if (this.pass != p) {
            this.pass = p;
            byte[] pp = paramsForPass(p);
            this.dX = pp[0];
            this.dY = pp[1];
            this.oX = pp[2];
            this.oY = pp[3];
            this.rows = this.imi.rows > this.oY ? (this.imi.rows + this.dY - 1 - this.oY) / this.dY : 0;
            this.cols = this.imi.cols > this.oX ? (this.imi.cols + this.dX - 1 - this.oX) / this.dX : 0;
            if (this.cols == 0) {
                this.rows = 0;
            }
            this.dXsamples = this.dX * this.imi.channels;
            this.oXsamples = this.oX * this.imi.channels;
        }
    }

    static byte[] paramsForPass(int p) {
        switch(p) {
            case 1:
                return new byte[] { 8, 8, 0, 0 };
            case 2:
                return new byte[] { 8, 8, 4, 0 };
            case 3:
                return new byte[] { 4, 8, 0, 4 };
            case 4:
                return new byte[] { 4, 4, 2, 0 };
            case 5:
                return new byte[] { 2, 4, 0, 2 };
            case 6:
                return new byte[] { 2, 2, 1, 0 };
            case 7:
                return new byte[] { 1, 2, 0, 1 };
            default:
                throw new PngjExceptionInternal("bad interlace pass" + p);
        }
    }

    int getCurrRowSubimg() {
        return this.currRowSubimg;
    }

    int getCurrRowReal() {
        return this.currRowReal;
    }

    int getPass() {
        return this.pass;
    }

    int getRows() {
        return this.rows;
    }

    int getCols() {
        return this.cols;
    }

    public int getPixelsToRead() {
        return this.getCols();
    }

    public int getBytesToRead() {
        return (this.imi.bitspPixel * this.getPixelsToRead() + 7) / 8;
    }

    public int getdY() {
        return this.dY;
    }

    public int getdX() {
        return this.dX;
    }

    public int getoY() {
        return this.oY;
    }

    public int getoX() {
        return this.oX;
    }

    public int getTotalRows() {
        if (this.totalRows == 0) {
            for (int p = 1; p <= 7; p++) {
                byte[] pp = paramsForPass(p);
                int rows = this.imi.rows > pp[3] ? (this.imi.rows + pp[1] - 1 - pp[3]) / pp[1] : 0;
                int cols = this.imi.cols > pp[2] ? (this.imi.cols + pp[0] - 1 - pp[2]) / pp[0] : 0;
                if (rows > 0 && cols > 0) {
                    this.totalRows += rows;
                }
            }
        }
        return this.totalRows;
    }

    public long getTotalRawBytes() {
        long bytes = 0L;
        for (int p = 1; p <= 7; p++) {
            byte[] pp = paramsForPass(p);
            int rows = this.imi.rows > pp[3] ? (this.imi.rows + pp[1] - 1 - pp[3]) / pp[1] : 0;
            int cols = this.imi.cols > pp[2] ? (this.imi.cols + pp[0] - 1 - pp[2]) / pp[0] : 0;
            int bytesr = (this.imi.bitspPixel * cols + 7) / 8;
            if (rows > 0 && cols > 0) {
                bytes += (long) rows * (1L + (long) bytesr);
            }
        }
        return bytes;
    }

    public int getCurrRowSeq() {
        return this.currRowSeq;
    }
}