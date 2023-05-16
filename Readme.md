Απόλλωνας-Πέτρος Καλλιπολίτης, ΑΜ 4963
Ιωάννης Γιαννακός, ΑΜ 4970
Έτος: 2023
Ανάκτηση Πληροφορίας 
2η Φάση / Τελική φάση

Στο υλοποιημένο σύστημα αναζήτησης τραγουδιών, ο χρήστης πληκτρολογεί στη δοθείσα μπάρα αναζήτησης, ό,τι επιθυμεί σχετικά με κάποιο τραγούδι και πατώντας το κουμπί "Search" το σύστημα του παρουσιάζει τα αποτελέσματα ανά 10 με την δυνατότητα να προχωρήσει στα επόμενα 10, πατώντας το κουμπι "Next 10 results", ενώ ταυτόχρονα του δίνεται και η επιλογή να εφαρμόσει κάποιο φίλτρο αναζήτησης. Με την επιλογή φίλτρου "No filter", παρουσιάζονται στον χρήστη όλα τα αποτελέσματα της αναζήτησης του, ανεξαρτήτως φίλτρου. Με τις επιλογές φίλτρων "Artist", "Title", "Album", "Year", "Date", "Lyric", εμφανίζονται τα αποτελέσματα της αναζήτησης του χρήστη φιλτραρισμένα ως προς τον καλλιτέχνη, τον τίτλο του τραγουδιού, το άλμπουμ στο οποίο ανήκει το τραγούδι, αλλά και την χρονολογία και ημερομηνία έκδοσης του αντίστοιχα. Επιπλέον, εφόσον ο χρήστης έχει αναζητήσει το τραγούδι που επιθυμεί, του εμφανίζονται τονισμένες με κίτρινο χρώμα στο αποτέλεσμα οι λέξεις κλειδιά. Ακόμα, πατώντας το κουμπί "Sort by Year" ταξινομούνται τα αποτελέσματα ανάλογα με τη χρονολογία έκδοσης του κάθε τραγουδιού, σε αύξουσα κλίμακα, από το παλιότερο μέχρι και το πιο καινούργιο. Τέλος, ο χρήστης μπορεί να προβάλλει και το ιστορικό αναζήτησης του επιλέγοντας το κουμπί "Search History". 

Για να μπορέσει ο χρήστης να χρησιμοποιήσει το υλοποιημένο πρόγραμμά μας, πρέπει να τρέξει το αρχείο GUI.java, καθώς εκεί βρίσκεται η main μέθοδος, αλλά και να κάνει import τις βιβλιοθήκες της lucene: lucene-analysis-common-9.5.0.jar, lucene-core-9.5.0.jar, lucene-queryparser-9.5.0.jar, όπως και την βιβλιοθήκη commons-io-2.11.0.

------------------------------------------------------------------------------------------------------------------------------------------
Σημαντικά:
Προκειμένου να λειτουργήσει σωστά το σύστημα πρέπει στο αρχείο GUI.java στην γραμμή 179 ο χρήστης να αλλάξει την τιμή της μεταβλητής filePath το οποίο αποθηκεύει το path του φακέλου στο οποίο βρίσκονται τα indexed files στον προσωπικό υπολογιστή του χρήστη. Προσοχή διότι τα περιεχόμενα του φακέλου αυτού θα διαγράφονται σε κάθε εκκίνηση του συστήματός μας. Επίσης, πρέπει να δημιουργηθεί ένας φάκελος με το όνομα inputFiles στον οποίο θα προσθέσετε το αρχείο Eminem.csv .
------------------------------------------------------------------------------------------------------------------------------------------
