@echo off
echo Running SHA512 for shainput.txt
echo The final hash for shainput.txt is: 
java -jar SHA512.jar data/shainput.txt
echo By comparison, the hash given in shaoutput.txt is: 
echo ddaf35a193617abacc417349ae20413112e6fa4e89a97ea20a9eeee64b55d39a2192992a274fc1a836ba3c23a3feebbd454d4423643ce80e2a9ac94fa54ca49f

echo Running SHA512 for shainput2.txt
echo The final hash for shainput2.txt is: 
java -jar SHA512.jar data/shainput2.txt
echo By comparison, the hash given in shaoutput2.txt is: 
echo 05d25ab314c10ff25219e9fa8ca23cf26923ad653796c027fb23501eeb8a6454029d2144d7146a5e24d123c5517201513994c04b85b3ffa81afb04d609138a62


echo Running SHA512 for shainput3.txt
echo The final hash for shainput3.txt is: 
java -jar SHA512.jar data/shainput3.txt
echo By comparison, the hash given in shaoutput3.txt is: 
echo aa0fb9e7f0917df9d1a9d801e3643ebc5520b4666d9432d0ebee81afc25d3d8e559e66399ad45fb3e8a0e1e8fc76c69c9a22292f516ce5044b475fb4d9f186bb