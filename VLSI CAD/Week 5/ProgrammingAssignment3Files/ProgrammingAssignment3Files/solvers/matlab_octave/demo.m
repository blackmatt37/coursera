% demonstrate how to solve (sparse) matrix in matlab

%%%%%%%%%%%%%%%%% solve a small matrix %%%%%%%%%%%%%%%%%
disp 'Solve a small matrix'

% first column is row, second column is column, thrid column is value
RCV = [
1   1   4;
1   2  -1;
2   1  -1;
2   2   4;
2   3  -1;
3   2  -1;
3   3   4
];

b = [3; 2; 3]

% convert to sparse matrix, use 'full' to get the full matrix
A = spconvert(RCV)

% use '\' to linear solve
x = A\b

%%%%%%%%%%%%%%%%% solve a psd matrix %%%%%%%%%%%%%%%%%
disp 'Solve a Positive Semi-Definite matrix'

% Loads the matrices
A = dlmread('../data/psd.txt');

% the first line is n and nnz
A = A(2:end, :);

% note that the subscript start from 1
% add 1 to each row and column
A(:, 1) = A(:, 1) + 1;
A(:, 2) = A(:, 2) + 1;
sp = spconvert(A);

b = dlmread('../data/b.txt');
x = sp\b

%%%%%%%%%%%%%%%%% solve a big matrix %%%%%%%%%%%%%%%%%
disp 'Solve a big matrix'

A = dlmread('../data/mat_helmholtz.txt');
A = A(2:end, :);
A(:, 1) = A(:, 1) + 1;
A(:, 2) = A(:, 2) + 1;
sp = spconvert(A);

n = size(sp, 1);
b = ones(n, 1);

x = sp\b
