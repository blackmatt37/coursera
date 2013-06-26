import java.io.*;
import java.util.*;

public class Placer
{
	public static void main(String[] adsda) throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader("toy1"));
		PrintStream ps = new PrintStream(new FileOutputStream("toy1.out"));
		String[] temp = br.readLine().split(" ");
		int G = Integer.parseInt(temp[0]);
		int N = Integer.parseInt(temp[1]);
		
		Map<Integer, ArrayList<Integer>> net = new HashMap<Integer, ArrayList<Integer>>();
		Node[] nodes = new Node[G];
		for(int i=0;i<G;i++)
		{
			temp = br.readLine().split(" ");
			int num = Integer.parseInt(temp[0])-1;
			nodes[i] = new Node(false);
			int tot = Integer.parseInt(temp[1]);
			for(int j=0;j<tot;j++)
			{
				int neti = Integer.parseInt(temp[2+j]);
				if(net.containsKey(neti))
					net.get(neti).add(num);
				else
				{
					ArrayList<Integer> array = new ArrayList<Integer>();
					array.add(num);
					net.put(neti,array);
				}
			}
		}
		int P = Integer.parseInt(br.readLine());
		Node[] pads = new Node[P];
		for(int i=0;i<P;i++)
		{
			temp = br.readLine().split(" ");
			int num = Integer.parseInt(temp[0])-1;
			nodes[num] = new Node(true);
			net.get(Integer.parseInt(temp[1])).add(num+G);
			double x = (double)Integer.parseInt(temp[2]);
			double y = (double)Integer.parseInt(temp[3]);
			nodes[num].x = x;
			nodes[num].y = y;
		}
		for(int p: net.keySet())
		{
			ArrayList<Integer> list = net.get(p);
			int size = list.size();
			for(int i=0;i<size;i++)
				for(int j=0;j<size;j++)
				{
					int start = list.get(i);
					int end = list.get(j);
					if(start >= G)
						continue;
					if(end >= G)
						nodes[start].con.add(new Connection(1/(size-1),end,true));
					else
						nodes[start].con.add(new Connection(1/(size-1),end,false));
				}
		}
		
		
	}
}
class Connection
{
	public int end;
	public boolean isPad;
	public double weight;
	public Connection(double weight, int con, boolean isPad)
	{
		this.weight = weight;
		this.end = con;
		this.isPad = isPad;
	}
}
class Node
{
	public Node(boolean isPad)
	{
		this.isPad = isPad;
		con = new ArrayList<Connection>();
	}
	public ArrayList<Connection> con;
	public boolean isPad;
	public double x,y;
	
}