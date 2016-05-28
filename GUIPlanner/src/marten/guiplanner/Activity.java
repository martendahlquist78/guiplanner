package marten.guiplanner;

public class Activity extends Log
{
	public int index = 0;
	public String text = "";
	public int left = 0;
	public int top = 0;

	public Activity(String text, int left, int top)
	{
		this.text = text;
		this.left = left;
		this.top = top;
	}
	
	public Activity(int index, String text, int left, int top)
	{
		this.index = index;
		this.text = text;
		this.left = left;
		this.top = top;
	}
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
}
