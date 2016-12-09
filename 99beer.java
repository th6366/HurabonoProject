class NinetyNineBottles{
	public static void main(String[] args){
		for(int i = 99; i >= 0; i--){
			switch(i){
                                case 0:
				        System.out.println("'No more bottles of beer on the wall, no more bottles of beer." );
                                        System.out.println("Go to the store and buy some more, 99 bottles of beer on the wall.");
				        break;
		                case 1:
				        System.out.println(i + "bottle of beer on the wall, " + i + "bottle of beer.");
				        System.out.println("Take one down and pass it around, no more bottles of beer on the wall.");
                                        break;
                                case 2:
                                        System.out.println(i + "bottles of beer on the wall, " + i + "bottles of beer.");
				        System.out.println("Take one down and pass it around, 1 bottle of beer on the wall.");
                                        break;
			        default:
				        System.out.println(i + "bottles of beer on the wall, " + i + "bottles of beer.");
				        System.out.println("Take one down and pass it around, " + (i-1) + "bottles of beer on the wall.");
			}
		}
	}
}
