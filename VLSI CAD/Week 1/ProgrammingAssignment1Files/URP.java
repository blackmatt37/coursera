import java.io.*;
import java.util.*;

public class URP
{
	public static boolean match(String[] c1, String[] c2)
	{
		for(int i=0;i<c1.length;i++)
		{
			if(!c1[i].equals(c2[i]))
				return false;
		}
		return true;
	}
	public static ArrayList<String[]> complement(ArrayList<String[]> F, int N)
	{
		/*
		System.out.println(F);
		for(int i=0;i<F.size();i++)
		{
			for(int j=i+1;j<F.size();j++)
			{
				if(match(F.get(i), F.get(j)))
					F.remove(j);
			}
		}
		*/
		if(F.size() == 0)
		{
			String[] temp = new String[N];
			Arrays.fill(temp,"11");
			ArrayList<String[]> p = new ArrayList<String[]>();
			p.add(temp);
			return p;
		}
		if(dontCare(F,N))
			return new ArrayList<String[]>();
		if(F.size() == 1)
		{
			ArrayList<String[]> list = new ArrayList<String[]>();
			String[] temp = new String[F.get(0).length];
			System.arraycopy(F.get(0),0,temp,0,F.get(0).length);
			for(int i=0;i<temp.length;i++)
			{
				if(temp[i].equals("11"))
					continue;
				String[] cube = new String[N];
				Arrays.fill(cube, "11");
				if(temp[i].equals("01"))
					cube[i] = "10";
				else if(temp[i].equals("10"))
					cube[i] = "01";
				list.add(cube);
			}
			return list;
		}
		
		ArrayList<Integer> binates = new ArrayList<Integer>();
		for(int i=0;i<N;i++)
		{
			//boolean flag = false;
			
			String first = "0";
			int j=0;
			ArrayList<String> checker = new ArrayList<String>();
			for(j=0;j<F.size();j++)
			{
				if(!F.get(j).equals("11"))
					checker.add(new String(F.get(j)[i]));
			}
			if(checker.contains("10") && checker.contains("01"))
				binates.add(i);
		}
		int SWITCH = 0;
		if(binates.size() >= 1)
		{
			int[] most = new int[N];
			for(int i : binates)
			{
				for(int j=0;j<F.size();j++)
				{
					if(!F.get(j)[i].equals("11"))
						most[i]++;
				}
			}
			
			int max = 0;
			for(int i=0;i<most.length;i++)
			{
				if(most[i] > max)
					max = most[i];
			}
			ArrayList<Integer> mostl = new ArrayList<Integer>();
			for(int i=0;i<most.length;i++)
				if(most[i] == max)
					mostl.add(i);
			if(mostl.size() == 1)
				SWITCH = new Integer(mostl.get(0));
			else
			{
				int[] most2 = new int[N];
				for(int i : mostl)
				{
					for(int j=0;j<F.size();j++)
					{
						if(F.get(j)[i].equals("01"))
							most2[i]++;
						else if(F.get(j)[i].equals("10"))
							most2[i]--;
					}
				}
				for(int i=0;i<most2.length;i++)
				{
					most2[i] = Math.abs(most2[i]);
				}
				
				int min = most2.length-1;
				for(int i=most2.length-1;i>=0;i--)
				{
					if(most2[i] <= most2[min] && binates.contains(i))
						min = i;
				}
				SWITCH = min;
				
				
			}
		}
		else
		{
			int[] most = new int[N];
			for(int i=0;i<N;i++)
			{
				for(int j=0;j<F.size();j++)
				{
					if(!F.get(j)[i].equals("11"))
						most[i]++;
				}
			}
			int max = 0;
			for(int i=0;i<most.length;i++)
			{
				if(most[i] > most[max])
					max = i;
			}
			SWITCH = max;
			
		}
		ArrayList<String[]> Pos = complement(pCofactor(F,SWITCH),N);
		ArrayList<String[]> Neg = complement(nCofactor(F,SWITCH),N);
		
		Pos = and(Pos,SWITCH,true);
		Neg = and(Neg,SWITCH,false);
		/*
		for(int i=0;i<Pos.size();i++)
		{
			for(int j=i+1;j<Pos.size();j++)
			{
				if(match(Pos.get(i), Pos.get(j)))
					Pos.remove(j);
			}
		}
		for(int i=0;i<Neg.size();i++)
		{
			for(int j=i+1;j<Neg.size();j++)
			{
				if(match(Neg.get(i), Neg.get(j)))
					Neg.remove(j);
			}
		}
		*/
		return or(Pos,Neg);
	}
	public static ArrayList<String[]> and(ArrayList<String[]> F, int x,boolean sign)
	{
		ArrayList<String[]> out = new ArrayList<String[]>();
		for(int i=0;i<F.size();i++)
		{
			String[] temp = new String[F.get(i).length];
			System.arraycopy(F.get(i),0,temp,0,F.get(i).length);
			if(!sign)
				temp[x] = "10";
			else
				temp[x] = "01";
			out.add(temp);
		}
		return out;
	}
	public static ArrayList<String[]> or(ArrayList<String[]> cl1, ArrayList<String[]> cl2)
	{
		ArrayList<String[]> temp = new ArrayList<String[]>();
		for(int i=0;i<cl1.size();i++)
			temp.add(cl1.get(i));
		for(int i=0;i<cl2.size();i++)
			temp.add(cl2.get(i));	
		return temp;
	}
	public static ArrayList<String[]> pCofactor(ArrayList<String[]> F, int x)
	{
		ArrayList<String[]> out = new ArrayList<String[]>();
		for(int i=0;i<F.size();i++)
		{
			String[] temp = new String[F.get(i).length];
			System.arraycopy(F.get(i),0,temp,0,F.get(i).length);
			if(temp[x].equals("01") || temp[x].equals("11"))
			{
				temp[x] = "11";
				out.add(temp);
			}
		}
		return out;
	}
	public static ArrayList<String[]> nCofactor(ArrayList<String[]> F, int x)
	{
		ArrayList<String[]> out = new ArrayList<String[]>();
		for(int i=0;i<F.size();i++)
		{
			String[] temp = new String[F.get(i).length];
			System.arraycopy(F.get(i),0,temp,0,F.get(i).length);
			if(temp[x].equals("10")|| temp[x].equals("11"))
			{
				temp[x] = "11";
				out.add(temp);
			}
		}
		return out;
	}
	public static boolean dontCare(ArrayList<String[]>  F, int N)
	{
		for(int i=0;i<F.size();i++)
		{
			String[] temp = new String[F.get(i).length];
			System.arraycopy(F.get(i),0,temp,0,F.get(i).length);
			boolean flag = false;
			for(int j=0;j<temp.length;j++)
			{
				if(!temp[j].equals("11"))
					flag = true;
			}
			if(!flag)
				return true;
		}
		return false;
	}
	public static void main(String[] ads) throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader("part5.cubes"));
		PrintStream ps = new PrintStream(new FileOutputStream("part5.out"));
		int N = Integer.parseInt(br.readLine());
		int M = Integer.parseInt(br.readLine());
		ArrayList<String[]> F = new ArrayList<String[]>();
		for(int i=0;i<M;i++)
		{
			String[] list = new String[N];
			Arrays.fill(list,"11");
			String[] temp = br.readLine().split(" ");
			for(int j=1;j<temp.length;j++)
			{
				int d = Integer.parseInt(temp[j]);
				if(d > 0)
					list[d-1] = "01";
				else
					list[Math.abs(d)-1]="10";
			}
			F.add(list);
		}
		F = complement(F,N);
		N = F.get(0).length;
		M = F.size();
		ps.println(N);
		ps.println(M);
		
		for(int i=0;i<M;i++)
		{
			String[] list = new String[F.get(i).length];
			System.arraycopy(F.get(i),0,list,0,F.get(i).length);
			ArrayList<Integer> thing = new ArrayList<Integer>();
			int counter = 0;
			for(int j=0;j<list.length;j++)
			{
				if(list[j].equals("01"))
				{
					thing.add(j+1);
					counter++;
				}
				else if(list[j].equals("10"))
				{
					thing.add((j+1)*-1);
					counter++;
				}
					
			}
			thing.add(0,counter);
			for(int j=0;j<thing.size();j++)
			{
				if(j == thing.size()-1)
					ps.print(thing.get(j));
				else
					ps.print(thing.get(j) + " ");
			}
			ps.println();
		}
	}

}