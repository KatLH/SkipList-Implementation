#compile
javac Hw01.java
#begin test steps
java Hw01 in5.txt >in5St.txt
diff in5St.txt in5Valid.txt
java Hw01 in5del1srch1.txt > in5del1srch1St.txt
diff in5del1srch1St.txt in5del1srch1Valid.txt
java Hw01 in5del2.txt > in5del2St.txt
diff in5del2St.txt in5del2Valid.txt
java Hw01 in10.txt > in10St.txt
diff in10St.txt in10Valid.txt
java Hw01 in100.txt > in100St.txt
diff in100St.txt in100Valid.txt
java Hw01 in100m5000.txt > in100m5000St.txt
diff in100m5000St.txt in100m5000Valid.txt
java Hw01 in10k-m5000.txt > in10k-m5000St.txt
diff in10k-m5000St.txt in10k-m5000Valid.txt
java Hw01 in1m-m1000.txt > in1m-m1000St.txt
diff in1m-m1000St.txt in1m-m1000Valid.txt
