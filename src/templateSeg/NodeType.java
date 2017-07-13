package templateSeg;

public class NodeType {
	public int start;
	public int end;
	public String type;
	
	public NodeType(String rule,int s,int e){
		type = rule;
		start = s;
		end = e;
	}

	@Override
	public String toString() {
		return "NodeType [type=" + type + ", start=" + start + ", end=" + end + "]";
	}

}
