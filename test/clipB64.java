void main(){
	Scanner in = new Scanner(System.in);
	while (true)
		System.out.printf("%s\n",new String(Base64.getDecoder().decode(in.nextLine())));
}