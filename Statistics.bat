java -classpath build statistics.Statistics -d s01 s02 s03 s04 s05 s06 s07 s08 s09 s10 s11 s12 s13 s14 s15 s16 s17 s18 s19 s20 s21 s22 s23 s24 s25 -m median mean quantile=0.25 quantile=0.25,0.75 quantile=0.25-0.75 range iqr -p bar box dot hist strip -t -a
echo Die R-Dateien liegen in src/statistics
pause