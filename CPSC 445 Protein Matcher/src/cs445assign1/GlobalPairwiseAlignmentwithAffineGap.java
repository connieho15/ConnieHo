package cs445assign1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import cs445assign1.Blosum;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class GlobalPairwiseAlignmentwithAffineGap {

	public static int d = 10;
	public static int e = 5;

	public static String unknown = "MAAALIRRLLRGLRTELAALDSAFPLLHAALAADHDVLPEDKFQEDTLHLKEKEGPQAFHALLSGLLTQDSTAALDRVLAKED"
			+ "YNLERKGRLQPVLDSFPKDVDLSQPRKGRKPPDAVPKALVPPRLPTKRKEASEEARAAAPAAALTPRGTASPGSQLKAKPPKKPESSAEQQRKLPLGA"
			+ "NDGLQTLSASVQRAVALSSGDVLPEGARGAVLEGALIQQVFESGGSKKCLQVGAGEFYTPSKFEDSGSGKNKARSSSGPKPLVRAKGAQGAAPGGEDA"
			+ "RLGQQGSVPAPLALPSDPQLHQKNEDEAVLLRDAGGELLADGPRAFHLALSPPALREIPSGTRCSSLQATLQELQPRAEEPRPQEPVETLPGLRSAGEE"
			+ "VLRGPPGEPLAGLDTTLVYKHLPAAPPSAAPLPGLDSSALHPLLVGPEGQQNLAPGARKGVGDGTDAVLIRTHAAAFHRHEFPAGTSRPGTGLRRSC"
			+ "SGDLTPKAPVEGVLAPSPARLAPGPAKDTASHEPALRDLESLLSEHTDGALQALQSLARPSAS";



	@SuppressWarnings("unchecked")
	public static void parseXml(){
		try {	
			File inputFile = new File("proteins.xml");
			DocumentBuilderFactory dbFactory 
			= DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("entry");

			List<KeyVal> results = new ArrayList<KeyVal>();
			int max1 = 0;
			int maxi1 = 0;
			String max1name = "";
			int max2 = 0;
			int maxi2 = 0;
			String max2name = "";
			int max3 = 0;
			int maxi3 = 0;
			String max3name = "";
			for (int temp = 0; temp < nList.getLength(); temp++) {
				
				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					int score = dynamic(eElement
							.getElementsByTagName("DNAseq")
							.item(0)
							.getTextContent(), unknown);
					
					String name = eElement.getAttribute("id").split(Pattern.quote("|"))[2];
					System.out.println("Index=" 
							+ temp + " Name=" 
							+ name + " Score=: " 
							 + score);

					if (max1 <= score){ 
						
						max3 = max2;
						maxi3 = maxi2;
						max3name = max2name;					
						
						max2 = max1;
						maxi2=maxi1;
						max2name = max1name;
						
						max1 = score;
						maxi1 = temp;
						max1name = name;
						
					} else {

						if (max2 <= score){ 
							max3 = max2;
							maxi3 = maxi2;
							max3name = max2name;
							
							max2name = name;
							max2 = score;
							maxi2 = temp;
						} else {

							if (max3 <= score){
								max3 = score;
								maxi3 = temp;
								max3name = name;
							}
						}

					}
				}

			}
			System.out.println(
					"Index=" + maxi1 + " " +    
							"Name=" + max1name + " " + 
							"Score=" + max1);
			System.out.println(
					"Index=" + maxi2 + " " +    
							"Name=" + max2name + " " + 
							"Score=" + max2);
			System.out.println(
					"Index=" + maxi3 + " " +    
							"Name=" + max3name + " " + 
							"Score=" + max3);
		}
		catch (Exception e) {
				e.printStackTrace();
			}

		}

		



	@SuppressWarnings("unchecked")
	public static int dynamic(String x, String y) {
		int[][] M = new int[x.length() + 1][y.length() + 1];
		int[][] Gx = new int[x.length() + 1][y.length() + 1];
		int[][] Gy = new int[x.length() + 1][y.length() + 1];

		// Corner
		M[0][0] = 0;
		Gx[0][0] = -1000000000;
		Gy[0][0] = -1000000000;

		// Top Row
		for (int i = 1; i < x.length(); i++) {
			M[i][0] = -1000000000;
			Gx[i][0] = -d-(e *(i-1));
			Gy[i][0] = -1000000000;

		}

		// Left Column
		for (int j = 1; j < y.length(); j++) {
			M[0][j] = -1000000000;
			Gx[0][j] = -1000000000;
			Gy[0][j] = -d-(e*(j-1));
		}

		for (int i = 1; i <= x.length(); i++) {
			for (int j = 1; j <= y.length(); j++) {

				// M(i,j)
				@SuppressWarnings("rawtypes")
				List Mcomp = new ArrayList(); 
				Mcomp.add(M[i-1][j-1] + Blosum.getDistance(x.charAt(i-1), y.charAt(j-1)));
				Mcomp.add(Gx[i-1][j-1] + Blosum.getDistance(x.charAt(i-1), y.charAt(j-1)));
				Mcomp.add(Gy[i-1][j-1] + Blosum.getDistance(x.charAt(i-1), y.charAt(j-1)));
				M[i][j] = (int) Collections.max(Mcomp);


				// Gx(i,j)
				List Gxcomp = new ArrayList(); 
				Gxcomp.add(M[i-1][j] -d);
				Gxcomp.add(Gx[i-1][j] -e);
				Gxcomp.add(Gy[i-1][j] -d);
				Gx[i][j] = (int) Collections.max(Gxcomp);


				// Gy(i,j)
				List Gycomp = new ArrayList(); 
				Gycomp.add(M[i][j-1] -d);
				Gycomp.add(Gx[i][j-1] -d);
				Gycomp.add(Gy[i][j-1] -e);
				Gy[i][j] = (int) Collections.max(Gycomp);

			}													
		}

		List Solcomp = new ArrayList(); 
		Solcomp.add(M[x.length()][y.length()]);
		Solcomp.add(Gx[x.length()][y.length()]);
		Solcomp.add(Gy[x.length()][y.length()]);
		return (int) Collections.max(Solcomp);
	}

	public static void main(String[] args) {
		parseXml();

		// Scanner scanner = new Scanner(System.in);
		// System.out.print("Please enter X: ");
		// String x = scanner.next();
		// System.out.println("Please enter Y:");
		// String y = scanner.next();
		// System.out.println("Max Score for Global Pairwise Alignment with Affine Gap Penalty: " + dynamic(x, y));
	}

}


