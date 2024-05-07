# Nama file utama (tanpa ekstensi)
MAIN=WordLadderGUI

# Compiler yang digunakan
JAVAC=javac

# Flags untuk compiler
JFLAGS=-g

# Target utama
.PHONY: default
default: $(MAIN).class

# Rule untuk membuat file .class dari file .java
%.class: %.java
	$(JAVAC) $(JFLAGS) $<

# Membersihkan file hasil kompilasi
.PHONY: clean
clean:
	del *.class

# Menjalankan program
.PHONY: run
run: $(MAIN).class
	java $(MAIN)
