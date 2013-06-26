// Demonstrate how to use the sparse matrix class and solve the linear system
// Compilation:
//   g++ -o demo demo.cpp solver.cpp
//
// Solves three matrices: 3x3, 20x20, and a big one 400x400


#include <cstdio>
#include <cstdlib>
#include <valarray>
using namespace std;

// this is the header file of solver class
#include "solver.h"

// write the matrix directly in code
void solve_small() {
  cout << "** small demonstration" << endl;
  coo_matrix A;
  int R[]    = {0, 0, 1, 1, 1, 2, 2};
  int C[]    = {0, 1, 0, 1, 2, 1, 2};
  double V[] = {4.0, -1.0, -1.0,  4.0, -1.0, -1.0, 4.0};
  A.n = 3;
  A.nnz = sizeof(R) / sizeof(int);
  A.row.resize(A.nnz);
  A.col.resize(A.nnz);
  A.dat.resize(A.nnz);
  
  A.row = valarray<int>(R, A.nnz);
  A.col = valarray<int>(C, A.nnz);
  A.dat = valarray<double>(V, A.nnz);

  // initialize as [1, 1, 1]
  valarray<double> x(1.0, A.n);
  valarray<double> b(A.n);
  A.matvec(x, b); // b = Ax

  cout << "b should equal [3,2,3]" << endl;
  print_valarray(b);

  // solve for x
  cout << "x = " << endl;
  A.solve(b, x);
  print_valarray(x);
}

// read in a positive semi definite matrix, and b
void solve_psd() {
  cout << "** PSD demonstration" << endl;
  coo_matrix psd;
  psd.read_coo_matrix("../data/psd.txt");
  valarray<double> x(psd.n);
  valarray<double> b(psd.n);

  // read in b
  ifstream ifs("../data/b.txt");
  for (int i = 0; i < psd.n; ++i) {
    ifs >> b[i];
  }
  ifs.close();

  cout << "b = " << endl;
  print_valarray(b);

  psd.solve(b, x);
  cout << "x = " << endl;
  print_valarray(x);
}

// read in a big data
void solve_big() {
  cout << "** big demonstration" << endl;
  coo_matrix big;
  big.read_coo_matrix("../data/mat_helmholtz.txt");
  valarray<double> x(big.n);
  
  // set the matrix b as ones;
  valarray<double> b(1.0, big.n);
  big.solve(b, x);
  
  cout << "x = " << endl;
  print_valarray(x);
}

int main(int argc, char *argv[]) {
  solve_small();
  solve_psd();
  solve_big();
  return 0;
}
