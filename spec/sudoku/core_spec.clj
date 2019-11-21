(ns sudoku.core-spec
  (:require [speclj.core :refer :all]
            [sudoku.core :refer :all]))

;This is the degenerate case.  A sudoku board of rank 1.  Such a board has only one possible solution
;which is [[1]].
(describe
  "a rank 1 game"
  (context
    "it has only one solution [[1]]"
    (it "is valid if solved or empty."
        (should (is-valid-board? 1 [[1]]))
        (should (is-valid-board? 1 [[nil]])))
    (it "is invalid if wrong size or digit other than 1"
        (should-not (is-valid-board? 1 [[1] [1]]))
        (should-not (is-valid-board? 1 [[1 1]]))
        (should-not (is-valid-board? 1 []))
        (should-not (is-valid-board? 1 [[]]))
        (should-not (is-valid-board? 1 [[2]])))
    (it "should detect the one solution"
        (should (is-solved-board? 1 [[1]]))
        (should-not (is-solved-board? 1 [[nil]])))
    (it "should solve [[]]"
        (should= [[1]] (solve-board 1 [[nil]])))))

(describe
  "a rank 2 game"
  (context
    "has groupings consisting of columns, rows, and sectors"
    (let [board [[1 2 3 4]
                 [2 3 4 1]
                 [4 3 2 1]
                 [3 2 1 4]]]
      (it "should extract rows"
          (should= [1 2 3 4] (extract-row 0 board))
          (should= [2 3 4 1] (extract-row 1 board)))
      (it "should extract columns"
          (should= [1 2 4 3] (extract-column 0 board))
          (should= [2 3 3 2] (extract-column 1 board)))
      (it "should extract sectors"
          (should= [1 2 2 3] (extract-sector 2 [0 0] board))
          (should= [3 4 4 1] (extract-sector 2 [1 0] board))
          (should= [2 1 1 4] (extract-sector 2 [1 1] board)))))
  (context
    "a grouping"
    (it "is invalid if the wrong size."
        (should-not (is-valid-grouping? 2 []))
        (should-not (is-valid-grouping? 2 [1 2 3 4 nil])))
    (it "is invalid if there are duplicate numbers"
        (should-not (is-valid-grouping? 2 [1 1 1 1]))
        (should-not (is-valid-grouping? 2 [1 1 2 3]))
        (should-not (is-valid-grouping? 2 [1 nil 2 1])))
    (it "is valid if correct size and no duplicates."
        (should (is-valid-grouping? 2 [1 2 3 4]))
        (should (is-valid-grouping? 2 [1 2 nil nil])))
    (it "is solved if valid and no nils."
        (should (is-solved-grouping? 2 [1 2 3 4]))
        (should-not (is-solved-grouping? 2 [1 2 nil 4]))))

  (context
    "Solutions can be detected."
    (it "should see this as solved."
        (should (is-solved-board? 2 [
                                     [1 2 3 4]
                                     [3 4 1 2]
                                     [2 3 4 1]
                                     [4 1 2 3]])))

    (it "should not see a board with nils as solved"
        (should-not (is-solved-board? 2 [
                                         [1 nil 3 4]
                                         [3 4 1 2]
                                         [2 3 4 1]
                                         [4 1 2 3]])))
    (it "should not see an invalid board as solved"
        (should-not (is-solved-board? 2 [
                                         [1 4 3 4]
                                         [3 2 1 2]
                                         [2 3 4 1]
                                         [4 1 2 3]]))))
  )

