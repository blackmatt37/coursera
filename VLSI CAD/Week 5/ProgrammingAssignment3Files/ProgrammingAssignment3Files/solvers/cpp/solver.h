#ifndef _SOLVER_H_
#define _SOLVER_H_

#include <stdio.h>
#include <stdlib.h>
#include <valarray>
#include <algorithm>
#include <iostream>
#include <fstream>

using namespace std;

class coo_matrix{
  // COOrdinate sparse matrix
  public:
    int n;
    int nnz;
    valarray<int> row;
    valarray<int> col;
    valarray<double> dat;

    void read_coo_matrix(const char *fname);
    void matvec(const valarray<double> &x, valarray<double> &y);
    void solve(const valarray<double> &b, valarray<double> &x);
};

template<typename T>
void print_valarray(valarray<T>& v) {
  for (size_t i = 0; i < v.size(); ++i) {
    cout << v[i] << endl;
  }
}

#endif
