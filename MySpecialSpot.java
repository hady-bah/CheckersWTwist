package application;

public class MySpecialSpot implements SpecialSpot {
	  private int row;
	  private int col;

	  public MySpecialSpot(int row, int col) {
	    this.row = row;
	    this.col = col;
	  }

	  public int getRow() {
	    return row;
	  }

	  public int getCol() {
	    return col;
	  }

	@Override
	public boolean isSpecial(int row, int col) {
		if(this.col==col && this.row==row) {
			return true;
		}else {
			return false;
		}
	}

	 
	}
