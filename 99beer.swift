for var i = 99; i >= 0; i-- {
    
    switch(i) {
    case 0:
        print("'No more bottles of beer on the wall, no more bottles of beer." )
        print("Go to the store and buy some more, 99 bottles of beer on the wall.")
    case 1:
        print("\(i)bottles of beer on the wall, \(i)bottles of beer on the wall")
        print("Take one down and pass it around, no more bottles of beer on the wall.")
    case 2:
        print("\(i)bottles of beer on the wall, \(i)bottles of beer on the wall")
        print("Take one down and pass it around, 1 bottle of beer on the wall.")
    default:
        print("\(i)bottles of beer on the wall, \(i)bottles of beer.")
        print("Take one down and pass it around, \(i-1) bottles of beer on the wall.");
    }
}
