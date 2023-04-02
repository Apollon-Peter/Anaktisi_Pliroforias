Απόλλωνας-Πέτρος Καλλιπολίτης AM:4963
Ιωάννης Γιαννακός AM:4970
Έτος: 2023
Ανάκτηση Πληροφορίας
1η φάση της εργασίας


(α)
Για την δημιουργία της μηχανής αναζήτησης τραγουδιών που θα κατασκευάσουμε, χρησιμοποιήσαμε την ιστοσελίδα Kaggle, που αποτελεί μία διαδικτυακή κοινότητα με επίκεντρο την επιστήμη των δεδομένων. Από εκεί επιλέξαμε και κατεβάσαμε μία συλλογή εγγράφων της μορφής CSV (Comma Separated Values), με όνομα "Lyrics", αποτελούμενη από 742 τραγούδια. Επιλέξαμε αυτή την μορφή αρχείων, διότι έτσι θα μας είναι πιο εύκολο να προσπελάσουμε τα δεδομένα και συνεπώς να τα επεξεργαστούμε κατάλληλα. Πιο αναλυτικά, στο αρχείο αυτό, περιέχονται με την ακόλουθη σειρά, το όνομα του καλλιτέχνη, ο τίτλος και οι στίχοι σε κάθενα από τα 742 τραγούδια. 

(β)
Με το σύστημα που θα υλοποιήσουμε, σκοπεύουμε να δημιουργήσουμε μία μηχανή αναζήτησης τραγουδιών, μέσω της οποίας θα δίνεται η δυνατότητα στον χρήστη να αναζητήσει, τόσο το όνομα του τραγουδιού όσο και του καλλιτέχνη, αλλά και στίχους αυτών. Διαθέσιμα προς αναζήτηση θα είναι αυτά τα 742 τραγούδια, τα οποία συμπεριλαμβάνονται στην συλλογή εγγραφών που επιλέξαμε.
Θα αναλύσουμε το αρχείο με τα τραγούδια σύμφωνα με τις 3 κατηγορίες πληροφοριών που περιέχει, οι οποίες έχουν ως χαρακτήρα διαχωρισμού (delimiter) το "," (κόμμα), εφόσον χρησιμοποιούμε CSV αρχείο. Θα δημιουργήσουμε μια λίστα για κάθε μια από τις 3 κατηγορίες, στις οποίες θα αποθηκεύουμε ως αντικείμενο, τις αντίστοιχες πληροφορίες του κάθε τραγουδιού (πεδία). Στη συνέχεια, θα δημιουργήσουμε tokens, με βάση τον διαχωριστικό χαρακτήρα " "(κένος χαρακτήρας), προκειμένου να δημιουργήσουμε το ευρετήριο όλων των λέξεων/όρων που εμπεριέχονται στα τραγούδια. Για παράδειγμα, αν το όνομα του τραγουδιού ή του καλλιτέχνη περιέχει πάνω από 1 λέξεις, θα τις αποθηκεύσουμε ξεχωριστά. Συνεπώς, η μονάδα εγγράφου μας θα είναι το CSV αρχείο με τα τραγούδια και τα αντίστοιχα πεδία της θα είναι ο καλλιτέχνης, ο τίτλος και οι στίχοι των τραγουδιών, όπως αναφέρθηκε και παραπάνω. Τελικώς, για την ένταξη όρων στο ευρετήριο, θα εφαρμόσουμε περιστολή (stemming) και λημματοποίηση (lemmatization), ετσι ώστε να περικόψουμε τις καταλήξεις, αλλά και να ανάγουμε τις λέξεις στις ρίζες τους. 
Προκειμένου ο χρήστης να μπορέσει να αναζητήσει το επιθυμητό τραγούδι, θα του παρέχουμε μία μπάρα αναζήτησης στην οποία θα μπορεί να εισάγει είτε το όνομα του καλλιτέχνη ή τον τίτλο του τραγουδιού είτε αποσπάσματα των στίχων του. Μάλιστα θα δίνεται η δυνατότητα στον χρήστη, να αναζητήσει αποκλειστικά σε κάποιο από αυτά τα τρία πεδία και θα διατηρείται ένα ιστορικό αναζήτησης προκειμένου να προτείνονται εναλλακτικά ερωτήματα. 
Μετά την αναζήτηση του χρήστη θα του παρουσιάζονται όλα τα σχετικά αποτελέσματα σε μορφή πίνακα, ανά 10 τη φορά, με την δυνατότητα ο χρήστης να μπορεί να προχωρήσει στα επόμενα ή στα προηγούμενα 10 αποτελέσματα. Η σειρά με την οποία θα εμφανίζονται τα αποτελέσματα θα βασίζεται ανάλογα με το πεδίο που βρίσκονται οι όροι, που ο χρήστης έχει αναζητήσει, οι οποίοι θα παρουσιάζονται με πιο τονισμένα γράμματα. Δηλαδή στα αποτελέσματα θα έχουν προτεραιότητα οι όροι αναζήτησης που βρίσκονται στο όνομα του καλλιτέχνη, στον τίτλο του τραγουδιού και στους στίχους αντίστοιχα. Στην διάταξη των αποτελεσμάτων θα λαμβάνουμε υπόψη και την απόσταση από την οποία βρίσκονται μεταξύ τους οι όροι αναζήτησης και θα δίνουμε προτεραιότητα όπου αυτοί έχουν την μικρότερη απόσταση μεταξύ τους. Τέλος η συχνότητα θα επηρεάσει και εκείνη την διάταξη των αποτελεσμάτων, δηλαδή οι όροι που εμφανίζονται πιο συχνά θα βρίσκονται σε υψηλότερη κατάταξη σε αυτά. Σύμφωνα με τα παραπάνω, αυτό συνιστά μία ομαδοποίηση των αποτελεσμάτων, σχετικά με το πεδίο τους, την απόσταση των όρων τους και την συχνότητα εμφάνισής τους.
Όσον αφορά τα πακέτα της Lucene που θα χρησιμοποιήσουμε, αρχικά για την αναζήτηση των όρων, τα πακέτα org.apache.lucene.analysis και org.apache.lucene.search είναι τα πιο κατάλληλα, διότι το πρώτο θα αναλάβει να μετατρέψει σε tokens την είσοδο του χρήστη ενώ το δεύτερο θα συμβάλλει στην αναζήτηση πολλαπλών όρων από τον χρήστη. Στη συνέχεια για την κατασκευή του ευρετηρίου θα μας είναι χρήσιμα τα πακέτα org.apache.lucene.document που θα βοηθήσει στον διαχωρισμό των πεδίων, το org.apache.lucene.analysis για να μετατρέψει σε tokens τους όρους που περιέχει το αρχείο csv που επιλέξαμε, τα org.apache.lucene.index και org.apache.lucene.store για την κατασκευή του ευρετηρίου, καθώς και τα πακέτα org.apache.lucene.codecs και org.apache.lucene.util, τα οποία συμβάλλουν στην αποκωδικοποίηση/κωδικοποίηση του ανεστραμένου ευρετηρίου και την αποθήκευσή του σε κάποιο data structure αντίστοιχα.



//analysis : για την αναζήτηση πιθανότατα και για το tokenization του ευρετηρίου ---
//document : το σπάει σε fields το αρχείο που θα του δώσουμε
//index : Για την κατασκευή του ευρετηρίου
//store : Για την κατασκευή ευρετηρίου/ιστορικού
//search : για την αναζήτηση σύνθετων όρων/φράσεων ---
//codecs : για την αποκωδικοποίηση/κωδικοποίηση ανεστραμένου ευρετηρίου
//util : data structures