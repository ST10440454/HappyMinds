package com.happyminds.app.data

import androidx.annotation.DrawableRes

data class ClassItem(val name: String, val timeRange: String, @DrawableRes val iconRes: Int, val isOrange: Boolean = true)
data class LessonItem(
    val id: String, val name: String, val duration: String, val progressPercent: Int = 0,
    @DrawableRes val iconRes: Int, val isOrange: Boolean = true, val subject: Subject,
    val grade: Int, val notes: String = "", val description: String = ""
)
data class SubjectProgress(val name: String, val level: String, val percent: Int, @DrawableRes val iconRes: Int, val isTeal: Boolean = false)
data class Achievement(val name: String, @DrawableRes val iconRes: Int, val isTeal: Boolean = false)
data class CommunityUpdate(val avatarInitials: String, val title: String, val timeAgo: String, val body: String)

enum class Subject(val displayName: String) {
    MATHS("Mathematics"), ENGLISH("English"),
    NATURAL_SCIENCE("Natural Science"), LIFE_SKILLS("Life Skills")
}

data class User(
    val id: String = "", val fullName: String = "", val email: String = "",
    val passwordHash: String = "", val childName: String = "", val age: String = "",
    val grade: String = "", val status: UserStatus = UserStatus.PENDING,
    val isVolunteer: Boolean = false, val helpType: String = "",
    val skills: String = "", val createdAt: Long = System.currentTimeMillis()
)
enum class UserStatus { PENDING, APPROVED, REJECTED }

object UserSession {
    var currentUser: User? = null
    private val progress = mutableMapOf<String, Int>()
    fun getLessonProgress(lessonId: String) = progress[lessonId] ?: 0
    fun updateLessonProgress(lessonId: String, pct: Int) { progress[lessonId] = pct }
    fun setAllProgress(map: Map<String, Int>) { progress.clear(); progress.putAll(map) }
    fun isLoggedIn() = currentUser != null
    fun clear() { currentUser = null; progress.clear() }
}

object Curriculum {
    val GRADES = 1..7

    fun getLessonsForGrade(grade: Int): List<LessonItem> = buildList {
        addAll(mathsFor(grade)); addAll(englishFor(grade))
        addAll(scienceFor(grade)); addAll(lifeSkillsFor(grade))
    }

    private fun L(id: String, name: String, dur: String, subject: Subject, grade: Int,
                  isOrange: Boolean, desc: String, notes: String) = LessonItem(
        id = id, name = name, duration = dur, progressPercent = 0,
        iconRes = com.happyminds.app.R.drawable.ic_book,
        isOrange = isOrange, subject = subject, grade = grade,
        notes = notes, description = desc
    )
    private fun mathsFor(g: Int) = when (g) {
        1 -> listOf(
            L("g1-m1","Counting to 20","10 min",Subject.MATHS,1,true,"Count numbers 1 to 20.","""
📚 LESSON: Counting to 20
🎯 What you will learn:
• Count forward from 1 to 20
• Recognise number symbols
• Count objects one by one

📖 NOTES:
Counting means saying numbers in the right order starting at 1.

Count 1 to 10:  1  2  3  4  5  6  7  8  9  10
Count 11 to 20: 11 12 13 14 15 16 17 18 19 20

💡 TIP: Touch each object as you count so you don't skip any!

✏️ PRACTICE:
Q1. What number comes after 7? ___
Q2. What number comes before 15? ___
Q3. Write the missing numbers: 5, 6, ___, 8, ___, 10
Q4. Count these and write the number: ★★★★★★★★★ = ___

✅ ANSWERS: Q1=8  Q2=14  Q3=7 and 9  Q4=9
"""),
            L("g1-m2","Shapes Around Us","12 min",Subject.MATHS,1,true,"Identify circles, squares, triangles and rectangles.","""
📚 LESSON: Shapes Around Us
🎯 What you will learn:
• Name 4 basic 2D shapes
• Describe shapes using sides and corners
• Find shapes in everyday objects

📖 NOTES:
CIRCLE   – round, no corners, no straight sides (coin, wheel)
SQUARE   – 4 equal sides, 4 corners (window tile)
RECTANGLE– 4 sides (2 long, 2 short), 4 corners (door, book)
TRIANGLE – 3 sides, 3 corners (pizza slice)

✏️ PRACTICE:
Q1. How many corners does a triangle have? ___
Q2. Which shape has no corners? ___
Q3. A square has ___ equal sides.
Q4. Name a shape you see in your classroom: ___

✅ ANSWERS: Q1=3  Q2=circle  Q3=4
"""),
            L("g1-m3","Simple Addition","15 min",Subject.MATHS,1,true,"Add small numbers up to 10.","""
📚 LESSON: Simple Addition
🎯 What you will learn:
• Understand what addition means
• Use the + and = signs
• Add numbers up to 10

📖 NOTES:
Addition means putting groups together to find the TOTAL.
We use + to add and = to show the answer.

Example: 🍎🍎 + 🍎🍎🍎 = 5
           2   +    3   = 5

KEY WORDS: add, plus, total, altogether, sum

✏️ PRACTICE:
Q1. 3 + 4 = ___     Q2. 5 + 2 = ___
Q3. 1 + 6 = ___     Q4. 4 + 4 = ___
Q5. Thabo has 3 sweets. Mum gives him 5 more. How many altogether? ___

✅ ANSWERS: Q1=7  Q2=7  Q3=7  Q4=8  Q5=8
""")
        )
        2 -> listOf(
            L("g2-m1","Place Value: Tens & Units","18 min",Subject.MATHS,2,true,"Understand tens and units in 2-digit numbers.","""
📚 LESSON: Place Value — Tens & Units
🎯 What you will learn:
• Know that 2-digit numbers have TENS and UNITS
• Break numbers into tens and units
• Write the value of each digit

📖 NOTES:
Example: 34
  3 tens = 30
  4 units = 4
  30 + 4 = 34

Example: 57
  5 tens = 50,  7 units = 7,  50 + 7 = 57

✏️ PRACTICE:
Q1. In 46, the digit 4 is in the ___ place.
Q2. In 82, the digit 2 is worth ___.
Q3. 65 = ___ + ___
Q4. 7 tens and 3 units = ___

✅ ANSWERS: Q1=tens  Q2=2  Q3=60+5  Q4=73
"""),
            L("g2-m2","Addition & Subtraction to 50","18 min",Subject.MATHS,2,true,"Add and subtract numbers up to 50.","""
📚 LESSON: Addition & Subtraction to 50
📖 NOTES:
ADDITION:  23 + 14 → Tens: 2+1=3, Units: 3+4=7 → 37
SUBTRACTION: 45 − 21 → Tens: 4−2=2, Units: 5−1=4 → 24

KEY WORDS: add, subtract, minus, difference, take away

✏️ PRACTICE:
Q1. 22 + 13 = ___    Q2. 31 + 15 = ___
Q3. 48 − 23 = ___    Q4. 50 − 17 = ___
Q5. A shop has 36 apples, sells 14. How many left? ___

✅ ANSWERS: Q1=35  Q2=46  Q3=25  Q4=33  Q5=22
"""),
            L("g2-m3","Introduction to Fractions","14 min",Subject.MATHS,2,true,"Understand halves and quarters.","""
📚 LESSON: Introduction to Fractions
📖 NOTES:
A FRACTION is an equal part of a whole.

½ = cut into 2 equal parts — each part is one half
¼ = cut into 4 equal parts — each part is one quarter

Example: A pizza cut into 4 equal slices
  Each slice = ¼    Two slices = 2/4 = ½

✏️ PRACTICE:
Q1. A bar cut into 2 equal pieces — each piece is ___
Q2. ¼ means 1 part out of ___ equal parts.
Q3. Which is bigger — ½ or ¼? ___
Q4. A square divided into 4 equal parts, 1 coloured = ___

✅ ANSWERS: Q1=½  Q2=4  Q3=½  Q4=¼
""")
        )
        3 -> listOf(
            L("g3-m1","Multiplication Basics","15 min",Subject.MATHS,3,true,"Multiply using groups, tables 2–5.","""
📚 LESSON: Multiplication Basics
📖 NOTES:
Multiplication is repeated addition.
3 × 4 = 4 + 4 + 4 = 12

× 2 TABLE              × 5 TABLE
1×2=2  6×2=12         1×5=5   6×5=30
2×2=4  7×2=14         2×5=10  7×5=35
3×2=6  8×2=16         3×5=15  8×5=40
4×2=8  9×2=18         4×5=20  9×5=45
5×2=10 10×2=20        5×5=25  10×5=50

TIP: 3×4 = 4×3 = 12 (order does not matter)

✏️ PRACTICE:
Q1. 4×3=___  Q2. 5×6=___  Q3. 2×9=___
Q4. 4 bags with 5 oranges each = ___ oranges

✅ ANSWERS: Q1=12  Q2=30  Q3=18  Q4=20
"""),
            L("g3-m2","Introduction to Division","18 min",Subject.MATHS,3,true,"Divide by sharing equally.","""
📚 LESSON: Introduction to Division
📖 NOTES:
Division = sharing equally OR grouping.

12 sweets shared between 3 children: 12 ÷ 3 = 4
20 books in groups of 5: 20 ÷ 5 = 4

Division is the OPPOSITE of multiplication:
3 × 4 = 12  →  12 ÷ 3 = 4  and  12 ÷ 4 = 3

✏️ PRACTICE:
Q1. 15÷3=___  Q2. 20÷4=___  Q3. 18÷2=___  Q4. 30÷5=___
Q5. 24 pencils shared among 4 learners = ___ each

✅ ANSWERS: Q1=5  Q2=5  Q3=9  Q4=6  Q5=6
"""),
            L("g3-m3","Fractions: Halves, Quarters & Thirds","15 min",Subject.MATHS,3,true,"Identify and compare ½, ¼ and ⅓.","""
📚 LESSON: Fractions — Halves, Quarters & Thirds
📖 NOTES:
½ = 1 out of 2 equal parts
⅓ = 1 out of 3 equal parts
¼ = 1 out of 4 equal parts

Finding a fraction of a number:
½ of 12 = 12÷2 = 6
⅓ of 12 = 12÷3 = 4
¼ of 12 = 12÷4 = 3

Comparing: ½ > ⅓ > ¼ (more pieces = smaller each piece)

✏️ PRACTICE:
Q1. ½ of 20=___  Q2. ¼ of 16=___  Q3. ⅓ of 9=___
Q4. Which is bigger: ½ or ⅓? ___
Q5. An 18 cm ribbon cut into 3 equal pieces — each = ___

✅ ANSWERS: Q1=10  Q2=4  Q3=3  Q4=½  Q5=6cm
""")
        )
        4 -> listOf(
            L("g4-m1","Multiplication Tables to 12","22 min",Subject.MATHS,4,true,"Master all times tables to 12.","""
📚 LESSON: Multiplication Tables to 12
📖 NOTES:
× 6 TABLE              × 7 TABLE
6×1=6   6×7=42        7×1=7   7×7=49
6×2=12  6×8=48        7×2=14  7×8=56
6×3=18  6×9=54        7×3=21  7×9=63
6×4=24  6×10=60       7×4=28  7×10=70
6×5=30  6×11=66       7×5=35  7×11=77
6×6=36  6×12=72       7×6=42  7×12=84

Multiplying 2-digit numbers:
34 × 3: (30×3) + (4×3) = 90 + 12 = 102

✏️ PRACTICE:
Q1. 7×8=___  Q2. 6×9=___  Q3. 12×11=___
Q4. 23×4=___  Q5. 12 eggs per box × 6 boxes = ___

✅ ANSWERS: Q1=56  Q2=54  Q3=132  Q4=92  Q5=72
"""),
            L("g4-m2","Long Division","20 min",Subject.MATHS,4,true,"Divide larger numbers using long division.","""
📚 LESSON: Long Division
📖 NOTES:
STEPS — D M S B: Divide, Multiply, Subtract, Bring down

EXAMPLE: 96 ÷ 4
  4 into 9 = 2 → 2×4=8 → 9−8=1 → bring down 6 → 16
  4 into 16 = 4 → 4×4=16 → 16−16=0
  ANSWER: 24

WITH REMAINDER: 79 ÷ 3
  3 into 7=2 r1 → bring down 9 → 19
  3 into 19=6 r1
  ANSWER: 26 remainder 1

✏️ PRACTICE:
Q1. 84÷4=___  Q2. 75÷5=___  Q3. 93÷3=___
Q4. 67÷2 = ___ remainder ___

✅ ANSWERS: Q1=21  Q2=15  Q3=31  Q4=33 r1
"""),
            L("g4-m3","Fractions & Decimals","18 min",Subject.MATHS,4,true,"Convert fractions to decimals.","""
📚 LESSON: Fractions & Decimals
📖 NOTES:
A decimal uses a dot to separate whole numbers from parts.

½ = 0.5    ¼ = 0.25    ¾ = 0.75
1/10 = 0.1   3/10 = 0.3   1/100 = 0.01

In 3.47:  3 = units,  4 = tenths,  7 = hundredths

Comparing: 0.6 > 0.3 (6 tenths > 3 tenths)

✏️ PRACTICE:
Q1. Write ¾ as a decimal: ___
Q2. Write 0.5 as a fraction: ___
Q3. Which is bigger: 0.7 or 0.4? ___
Q4. Write 7/10 as a decimal: ___

✅ ANSWERS: Q1=0.75  Q2=½  Q3=0.7  Q4=0.7
""")
        )
        5 -> listOf(
            L("g5-m1","Fraction Operations","22 min",Subject.MATHS,5,true,"Add and subtract fractions.","""
📚 LESSON: Fraction Operations
📖 NOTES:
SAME DENOMINATOR: just add/subtract numerators
  3/8 + 2/8 = 5/8      7/9 − 4/9 = 3/9

DIFFERENT DENOMINATORS: find LCM first
  1/3 + 1/4 → LCM=12 → 4/12 + 3/12 = 7/12

SIMPLIFYING: divide both by HCF
  6/8 ÷ 2 = 3/4

✏️ PRACTICE:
Q1. 2/5+1/5=___  Q2. 5/6−1/6=___
Q3. 1/2+1/4=___  Q4. 3/4−1/3=___
Q5. Simplify 4/6: ___

✅ ANSWERS: Q1=3/5  Q2=4/6=2/3  Q3=3/4  Q4=5/12  Q5=2/3
"""),
            L("g5-m2","Area & Perimeter","18 min",Subject.MATHS,5,true,"Calculate area and perimeter of rectangles.","""
📚 LESSON: Area & Perimeter
📖 NOTES:
PERIMETER = distance around the shape
  Formula: P = 2 × (length + width)
  Example: 8cm × 3cm rectangle → P = 2×(8+3) = 22cm

AREA = surface covered
  Formula: A = length × width
  Example: 8cm × 3cm rectangle → A = 8×3 = 24cm²

SQUARE: P = 4 × side     A = side × side

Units: Perimeter → cm/m    Area → cm²/m²

✏️ PRACTICE:
Q1. Rectangle 10m × 5m → Perimeter=___  Area=___
Q2. Square side 6cm → Perimeter=___  Area=___
Q3. Floor 7m × 4m — carpet needed? ___

✅ ANSWERS: Q1=30m,50m²  Q2=24cm,36cm²  Q3=28m²
"""),
            L("g5-m3","Large Number Operations","20 min",Subject.MATHS,5,true,"Multiply and divide large numbers.","""
📚 LESSON: Large Number Operations
📖 NOTES:
MULTIPLICATION — column method:
  243 × 12 = (243×2) + (243×10) = 486 + 2430 = 2916

DIVISION — long division:
  1248 ÷ 4: work digit by digit → 312

ESTIMATING: round first
  243 × 12 ≈ 240 × 12 = 2880 (close to 2916 ✓)

✏️ PRACTICE:
Q1. 124×3=___   Q2. 216×4=___
Q3. 840÷7=___   Q4. 936÷4=___
Q5. Estimate: 398×5 ≈ ___

✅ ANSWERS: Q1=372  Q2=864  Q3=120  Q4=234  Q5≈2000
""")
        )
        6 -> listOf(
            L("g6-m1","Ratio & Rate","22 min",Subject.MATHS,6,true,"Understand and use ratios and rates.","""
📚 LESSON: Ratio & Rate
📖 NOTES:
RATIO: compares two quantities with the same unit.
  3 red, 5 blue → ratio = 3:5
  Simplify: 6:10 ÷ 2 = 3:5

RATE: compares two DIFFERENT units.
  Speed: 60km/h    Price: R5 per litre

Solving: 3 litres cost R15 → 1 litre = R15÷3 = R5

✏️ PRACTICE:
Q1. Simplify 8:12 = ___
Q2. Car travels 300km in 3 hours. Speed? ___
Q3. 5 pens cost R20. 1 pen costs? ___
Q4. Class of 30, girls to boys = 2:3. How many girls? ___

✅ ANSWERS: Q1=2:3  Q2=100km/h  Q3=R4  Q4=12
"""),
            L("g6-m2","Percentages","20 min",Subject.MATHS,6,true,"Calculate percentages in everyday life.","""
📚 LESSON: Percentages
📖 NOTES:
Percent = "per hundred" (out of 100)

CONVERSIONS:
10% = 1/10 = 0.1      25% = 1/4 = 0.25
50% = 1/2 = 0.5       75% = 3/4 = 0.75

Finding % of an amount:
  20% of R300 = 0.2 × 300 = R60

Discount: R200 shirt, 15% off
  Discount = 15% × 200 = R30  →  New price = R170

✏️ PRACTICE:
Q1. 25% of 80=___
Q2. Write 0.4 as a percentage: ___
Q3. R500 increases by 10%. New price? ___
Q4. Scored 18/20 = ___% 

✅ ANSWERS: Q1=20  Q2=40%  Q3=R550  Q4=90%
"""),
            L("g6-m3","Geometry: Angles","18 min",Subject.MATHS,6,true,"Measure and classify angles.","""
📚 LESSON: Geometry — Angles
📖 NOTES:
TYPES OF ANGLES:
  Acute     < 90°
  Right     = 90°  (square corner)
  Obtuse    90° – 180°
  Straight  = 180°
  Reflex    180° – 360°
  Revolution = 360°

KEY RULES:
• Angles on a straight line = 180°
• Angles in a triangle = 180°
• Angles around a point = 360°

Example: 65° + x° = 180°  →  x = 115°

✏️ PRACTICE:
Q1. Name an angle of 130°: ___
Q2. Triangle angles: 60°, 80°, ___°
Q3. Angles on a line: 47° and ___°
Q4. Is 200° acute, obtuse or reflex? ___

✅ ANSWERS: Q1=obtuse  Q2=40°  Q3=133°  Q4=reflex
""")
        )
        else -> listOf(
            L("g7-m1","Algebraic Expressions","25 min",Subject.MATHS,7,true,"Simplify and evaluate algebraic expressions.","""
📚 LESSON: Algebraic Expressions
📖 NOTES:
Variable = letter for unknown (x, y)
Term = number or variable (3x, 5)
Coefficient = number in front (3 in 3x)
Constant = fixed number (7)
Like terms = same variable (3x and 5x)

COLLECTING LIKE TERMS:
  3x + 5x = 8x
  5x + 2y − 3x + 4y = 2x + 6y

SUBSTITUTION: x=3 → 4x−1 = 4(3)−1 = 11

✏️ PRACTICE:
Q1. 7m+3m=___
Q2. 6p−2p+4q=___
Q3. a=4 → 3a+2=___
Q4. x=5 → 2x²−3=___

✅ ANSWERS: Q1=10m  Q2=4p+4q  Q3=14  Q4=47
"""),
            L("g7-m2","Linear Equations","22 min",Subject.MATHS,7,true,"Solve one- and two-step linear equations.","""
📚 LESSON: Linear Equations
📖 NOTES:
GOLDEN RULE: Whatever you do to one side, do to the other.

ONE-STEP:
  x+7=12 → x=5      y−3=8 → y=11
  3n=15  → n=5      m/4=6 → m=24

TWO-STEP:
  2x+3=11 → 2x=8 → x=4
  CHECK: 2(4)+3=11 ✓

✏️ PRACTICE:
Q1. x+9=14 → x=___    Q2. y−5=7 → y=___
Q3. 4a=28 → a=___     Q4. 3n+2=17 → n=___
Q5. 2m−6=10 → m=___

✅ ANSWERS: Q1=5  Q2=12  Q3=7  Q4=5  Q5=8
"""),
            L("g7-m3","Geometric Figures","20 min",Subject.MATHS,7,true,"Properties of triangles and quadrilaterals.","""
📚 LESSON: Geometric Figures
📖 NOTES:
TRIANGLES by SIDES:
  Equilateral — all 3 sides equal, all angles 60°
  Isosceles   — 2 equal sides, 2 equal base angles
  Scalene     — no equal sides

TRIANGLES by ANGLES:
  Acute / Right / Obtuse

QUADRILATERALS:
  Square       — 4 equal sides, 4×90°
  Rectangle    — 2 pairs equal sides, 4×90°
  Parallelogram— 2 pairs parallel sides
  Rhombus      — 4 equal sides, no right angles
  Trapezium    — 1 pair parallel sides

RULES:
  Triangle angles = 180°
  Quadrilateral angles = 360°

✏️ PRACTICE:
Q1. Triangle: 50°+70°+___°=180°
Q2. 4 equal sides, no right angles = ___
Q3. Isosceles triangle, top angle 40° → base angles = ___

✅ ANSWERS: Q1=60°  Q2=rhombus  Q3=70° each
""")
        )
    }
    private fun englishFor(g: Int) = when (g) {
        1 -> listOf(
            L("g1-e1","Phonics: Alphabet Sounds","12 min",Subject.ENGLISH,1,false,"Learn the sound each letter makes.","""
📚 LESSON: Phonics — Alphabet Sounds
📖 NOTES:
Every letter has a NAME and a SOUND.

a=/a/ apple   b=/b/ ball    c=/k/ cat    d=/d/ dog
e=/e/ egg     f=/f/ fish    g=/g/ goat   h=/h/ hat
i=/i/ insect  j=/j/ jam     k=/k/ kite   l=/l/ lion
m=/m/ mat     n=/n/ net     o=/o/ orange p=/p/ pen
r=/r/ rat     s=/s/ sun     t=/t/ tap    u=/u/ umbrella
v=/v/ van     w=/w/ water

BLENDING: c+a+t = cat    d+o+g = dog

✏️ PRACTICE:
Q1. What sound does 'm' make? ___
Q2. Blend: b+u+s = ___
Q3. What letter makes the /f/ sound? ___
Q4. Write a word starting with 's': ___

✅ ANSWERS: Q1=/m/  Q2=bus  Q3=f
"""),
            L("g1-e2","Sight Words","10 min",Subject.ENGLISH,1,false,"Read common sight words.","""
📚 LESSON: Sight Words
📖 NOTES:
Sight words must be recognised at a GLANCE — they appear very often in books.

GRADE 1 SIGHT WORDS — learn these 20:
the  a   and  to   is   in   it   of   you  he
she  we  are  was  I    my   on   at   his  they

Using sight words in sentences:
"The cat is on the mat."
"I am a boy."
"She and he are my friends."

✏️ PRACTICE:
Q1. Circle the sight words: [the] [dog] [and] [big] [is]
Q2. Fill in: ___ cat sat on ___ mat.
Q3. Write a sentence using 'I' and 'am': ___

✅ ANSWERS: Q1=the, and, is   Q2=The / a
"""),
            L("g1-e3","Short Vowel Sounds","12 min",Subject.ENGLISH,1,false,"Practise the five short vowel sounds.","""
📚 LESSON: Short Vowel Sounds
📖 NOTES:
The 5 VOWELS: A  E  I  O  U

SHORT VOWEL WORDS:
/a/ — cat, bag, hat, man
/e/ — bed, hen, leg, pen
/i/ — big, pin, sit, tip
/o/ — dog, hot, mop, top
/u/ — bus, cup, mud, run

CVC pattern (Consonant-Vowel-Consonant):
b+a+g = bag    p+i+n = pin    c+o+t = cot

✏️ PRACTICE:
Q1. What is the vowel in 'cat'? ___
Q2. Write a short /e/ word: ___
Q3. Fill in the missing vowel: b_g = ___
Q4. Sort: pit, hat, sit, bat → /a/: ___   /i/: ___

✅ ANSWERS: Q1=a  Q2=e.g.bed  Q3=a  Q4=hat,bat / pit,sit
""")
        )
        2 -> listOf(
            L("g2-e1","Phonics: Blends & Digraphs","15 min",Subject.ENGLISH,2,false,"Learn consonant blends and digraphs.","""
📚 LESSON: Phonics — Blends & Digraphs
📖 NOTES:
BLENDS (2 sounds blended):
bl=black  br=bread  cl=clap  dr=drum  fl=flag
gr=green  pl=play   st=stop  tr=trip

DIGRAPHS (2 letters, 1 new sound):
sh=/sh/ ship    ch=/ch/ chip    th=/th/ this
wh=/w/  when    ph=/f/  phone

✏️ PRACTICE:
Q1. Write a word with blend 'st': ___
Q2. Which digraph makes /sh/? ___
Q3. Complete: ___ip (for a boat) = ___
Q4. Circle the digraph: phone → ___

✅ ANSWERS: Q1=stop  Q2=sh  Q3=ship  Q4=ph
"""),
            L("g2-e2","Building Sentences","14 min",Subject.ENGLISH,2,false,"Write complete simple sentences.","""
📚 LESSON: Building Sentences
📖 NOTES:
A SENTENCE must have:
1. Capital letter at the start
2. A SUBJECT (who/what)
3. A VERB (doing/action word)
4. End punctuation  .  ?  !

Types:
Statement   → The dog sleeps.
Question    → Where is the dog?
Exclamation → The dog is huge!

"The girl runs." — 'girl' = subject,  'runs' = verb

✏️ PRACTICE:
Q1. Fix: the boy kicks the ball → ___
Q2. Add end mark: Where are my shoes ___
Q3. Underline the verb: The bird sings loudly.
Q4. Write your own sentence about school: ___

✅ ANSWERS: Q1=The boy kicks the ball.  Q2=?  Q3=sings
"""),
            L("g2-e3","Story Time — Retelling","18 min",Subject.ENGLISH,2,false,"Retell a story in your own words.","""
📚 LESSON: Story Time — Retelling
📖 NOTES:
Every story has THREE parts:
🟢 BEGINNING — who, where, when
🟡 MIDDLE    — the problem/adventure
🔴 END       — the solution

STORY ELEMENTS:
Characters / Setting / Problem / Solution

RETELLING WORDS: First… Then… Next… Finally…

MINI STORY:
"Sipho wanted to fly a kite. The wind was too weak.
He waited until afternoon. Then a big gust came and
his kite soared into the sky! He was so happy."

✏️ PRACTICE:
Q1. Who is the character? ___
Q2. What was Sipho's problem? ___
Q3. How was it solved? ___
Q4. Retell using: First… Then… Finally…

✅ ANSWERS: Q1=Sipho  Q2=wind too weak  Q3=waited for wind
""")
        )
        3 -> listOf(
            L("g3-e1","Reading Comprehension","18 min",Subject.ENGLISH,3,false,"Read a passage and answer questions.","""
📚 LESSON: Reading Comprehension
📖 NOTES:
TYPES OF QUESTIONS:
Literal      — answer IS in the text
Inferential  — use clues + think
Vocabulary   — explain meaning of a word

STRATEGY — SQ3R:
Survey → Question → Read → Recall → Review

PASSAGE:
"Amara lived near a great forest. Every morning she
collected fruit with her grandmother. One day she
wandered too far and got lost. She remembered what her
grandmother said: 'Follow the river home.' She followed
the river and arrived safely just before sunset."

✏️ PRACTICE:
Q1. Where did Amara live? ___
Q2. Who did she collect fruit with? ___
Q3. What problem did she face? ___
Q4. What advice did she follow? ___
Q5. Find a word meaning 'walked slowly away': ___

✅ ANSWERS: Q1=near a forest  Q2=grandmother  Q3=got lost  Q4=follow the river  Q5=wandered
"""),
            L("g3-e2","Nouns, Verbs & Adjectives","15 min",Subject.ENGLISH,3,false,"Identify parts of speech.","""
📚 LESSON: Nouns, Verbs & Adjectives
📖 NOTES:
NOUN — naming word (person, place, thing, animal)
  People: teacher, Sipho   Places: school, Johannesburg
  Things: book, apple      Animals: lion, dog

VERB — action/doing word
  run, jump, read, eat, write, is, are

ADJECTIVE — describes a noun
  big, small, red, happy, cold, soft

"The tall teacher read a funny book."
    Adj+Noun    Verb   Adj+Noun

✏️ PRACTICE:
Q1. Noun from: "The happy dog barked loudly." → ___
Q2. Verb from: "She runs to school." → ___
Q3. Adjective from: "A cold wind blew." → ___
Q4. Label N, V or Adj: friendly___ park___ swim___

✅ ANSWERS: Q1=dog  Q2=runs  Q3=cold  Q4=Adj,N,V
"""),
            L("g3-e3","Creative Writing","20 min",Subject.ENGLISH,3,false,"Write a short story.","""
📚 LESSON: Creative Writing
📖 NOTES:
STORY PLANNING:
🟢 BEGINNING: Who? Where? When? Hook the reader!
🟡 MIDDLE: Problem or event
🔴 END: Solution, how characters feel

STRONG BEGINNINGS (hooks):
"It was the strangest thing Lebo had ever seen."
"Nobody believed a fish could talk — until today."

USE YOUR 5 SENSES: See/Hear/Smell/Taste/Touch

CONNECTIVES: First… then… next… after that… finally…
because… however… although… suddenly…

✏️ WRITING TASK:
Write a story (8–10 sentences): "The day I found a mysterious box"

Plan:
Beginning: Who found it? Where? ___
Middle: What was inside? What happened? ___
End: How did it turn out? ___
""")
        )
        4 -> listOf(
            L("g4-e1","Reading Comprehension","20 min",Subject.ENGLISH,4,false,"Infer meaning from longer passages.","""
📚 LESSON: Reading Comprehension — Grade 4
📖 NOTES:
MAIN IDEA — what the passage is mostly about
SUPPORTING DETAILS — facts that back up the main idea
INFERENCE — clues in text + your own knowledge
SYNONYM — similar meaning word    ANTONYM — opposite meaning

PASSAGE:
"Honeybees are remarkable insects. A single hive can hold
up to 80 000 bees. Worker bees collect nectar from flowers
and convert it into honey. The queen bee can lay up to
2 000 eggs per day. Without bees, many plants could not
reproduce because bees carry pollen from flower to flower."

✏️ PRACTICE:
Q1. Main idea of the passage? ___
Q2. How many eggs can a queen lay daily? ___
Q3. Why are bees important to plants? ___
Q4. Synonym for 'remarkable': ___
Q5. What would happen to plants if all bees died? ___

✅ ANSWERS: Q1=honeybees are remarkable/important  Q2=2000  Q3=carry pollen  Q4=amazing  Q5=many wouldn't reproduce
"""),
            L("g4-e2","Grammar: Tenses","18 min",Subject.ENGLISH,4,false,"Use past, present and future tense correctly.","""
📚 LESSON: Grammar — Tenses
📖 NOTES:
PRESENT: She walks. / She is walking.
PAST:    She walked. / She was walking.
FUTURE:  She will walk. / She is going to walk.

IRREGULAR PAST TENSE (memorise!):
go→went  run→ran  eat→ate  see→saw
is→was   have→had  write→wrote  come→came

✏️ PRACTICE:
Q1. Past tense: "She eats an apple." → ___
Q2. Future tense: "I play football." → ___
Q3. Tense of: "They were singing." → ___
Q4. Correct: "Yesterday I go to the shop." → ___

✅ ANSWERS: Q1=She ate an apple.  Q2=I will play football.  Q3=past continuous  Q4=Yesterday I went to the shop.
"""),
            L("g4-e3","Paragraph Writing","22 min",Subject.ENGLISH,4,false,"Structure paragraphs with topic and supporting sentences.","""
📚 LESSON: Paragraph Writing
📖 NOTES:
PARAGRAPH STRUCTURE:
1. TOPIC SENTENCE — states the main idea
2. SUPPORTING SENTENCES — details and examples
3. CONCLUDING SENTENCE — wraps up

EXAMPLE:
"Dogs make wonderful pets. [TOPIC] They are loyal and love
spending time with their owners. Dogs can be trained to
follow commands and help people. [SUPPORT] For these reasons,
dogs are one of the most popular animals in the world." [CONCLUSION]

LINKING WORDS: Firstly… Secondly… Furthermore…
In addition… However… Therefore… In conclusion…

✏️ WRITING TASK:
Write a paragraph (5–6 sentences) on ONE topic:
a) My favourite season
b) Why reading is important
c) A sport I enjoy
""")
        )
        5 -> listOf(
            L("g5-e1","Poetry: Rhyme & Rhythm","18 min",Subject.ENGLISH,5,false,"Identify rhyme and rhythm in poetry.","""
📚 LESSON: Poetry — Rhyme & Rhythm
📖 NOTES:
RHYME: words with the same ending sound (cat/hat/sat)

RHYME SCHEME: pattern shown with letters
  "Roses are red" (A)     "Violets are blue" (B)
  "Sugar is sweet" (C)    "And so are you" (B)
  Scheme = ABCB

POETIC DEVICES:
  Simile        — "like" or "as": "Her voice is like honey."
  Metaphor      — one thing IS another: "He is a shining star."
  Alliteration  — same starting sounds: "Sally sells seashells"
  Personification — human quality to non-human: "The sun smiled."

POEM — Read and analyse:
"The wind is a lion, fierce and free, (A)
It tosses the branches of every tree. (A)
It whispers and howls through the dark of night, (B)
And vanishes gently at morning light." (B)

✏️ PRACTICE:
Q1. Rhyme scheme? ___
Q2. Find a metaphor: ___
Q3. Word meaning 'disappears': ___
Q4. Two rhyming pairs: ___

✅ ANSWERS: Q1=AABB  Q2=wind is a lion  Q3=vanishes  Q4=free/tree, night/light
"""),
            L("g5-e2","Punctuation Mastery","16 min",Subject.ENGLISH,5,false,"Use commas, apostrophes and speech marks correctly.","""
📚 LESSON: Punctuation Mastery
📖 NOTES:
COMMA ,
  List:     I bought apples, oranges, bananas and grapes.
  Intro:    However, she disagreed.
  Compound: He was tired, but he kept running.

APOSTROPHE '
  Contraction: do not→don't   I am→I'm   it is→it's
  Possession:  the dog's bone (one dog)
               the dogs' bones (many dogs)
  ⚠️ its (belonging) has NO apostrophe!

SPEECH MARKS " "
  "What time is it?" she asked.
  He replied, "It is half past three."
  Rules: capital to start spoken words; punctuation INSIDE closing mark

✏️ PRACTICE:
Q1. Add commas: I need milk eggs bread and butter.
Q2. Contraction: they are → ___
Q3. Apostrophe: The girls bag was stolen. → ___
Q4. Punctuate: she said i am going home → ___

✅ ANSWERS: Q1=milk, eggs, bread  Q2=they're  Q3=The girl's bag  Q4="I am going home," she said.
"""),
            L("g5-e3","Essay Writing","22 min",Subject.ENGLISH,5,false,"Write a structured essay.","""
📚 LESSON: Essay Writing
📖 NOTES:
ESSAY STRUCTURE:
1️⃣ INTRODUCTION
   • Hook / Background / Thesis statement

2️⃣ BODY (2–3 paragraphs)
   • Topic sentence + Evidence + Explanation

3️⃣ CONCLUSION
   • Restate thesis + Summarise + Closing thought

LINKING WORDS:
Intro: Firstly, To begin with, It is evident that
Body:  Furthermore, In addition, On the other hand
Conc:  In conclusion, Therefore, To summarise

✏️ WRITING TASK:
Write a 3-paragraph essay:
"Schools should have longer lunch breaks."

Para 1 (Intro): Hook + your position
Para 2 (Body):  Two reasons + examples
Para 3 (Concl): Summary + final thought
""")
        )
        6 -> listOf(
            L("g6-e1","Formal vs Informal Language","20 min",Subject.ENGLISH,6,false,"Choose appropriate language for different contexts.","""
📚 LESSON: Formal vs Informal Language
📖 NOTES:
INFORMAL (casual — friends, texts):
  Short forms: gonna, wanna, yeah, hey
  "Hey! Wanna hang out later?"

FORMAL (professional — essays, letters, work):
  Full words: going to, want to, yes
  "Good day. Would you be available to meet this afternoon?"

COMPARISONS:
Hi/Hey → Dear Sir / Good day
Can't  → Cannot
Thanks a lot → Thank you sincerely
I wanna go → I would like to go

✏️ PRACTICE:
Q1. Is this formal or informal? "Yo, the party was lit!" ___
Q2. Rewrite formally: "I wanna know if you're coming." ___
Q3. Give TWO situations where formal language is needed: ___

✅ ANSWERS: Q1=informal  Q2=I would like to know if you are attending.
"""),
            L("g6-e2","Text Types","18 min",Subject.ENGLISH,6,false,"Identify and write different text types.","""
📚 LESSON: Text Types
📖 NOTES:
Narrative    — entertain/tell story → characters, plot, setting
Report/Info  — inform → facts, headings, present tense, impersonal
Persuasive   — convince → arguments, evidence, rhetorical questions
Procedure    — explain how to do → numbered steps, imperative verbs
Letter/Email — communicate → greeting, body, sign-off

PERSUASIVE TECHNIQUES:
  Rhetorical question: "Don't you want a better future?"
  Repetition: "We must act. We must act now."
  Statistics: "9 out of 10 children benefit from sport."

✏️ PRACTICE:
Q1. What text type explains how to make bread? ___
Q2. Which text type uses a first-person narrator? ___
Q3. Write ONE persuasive sentence about healthy eating: ___

✅ ANSWERS: Q1=procedure  Q2=narrative
"""),
            L("g6-e3","Idioms & Figurative Language","16 min",Subject.ENGLISH,6,false,"Interpret idioms, similes and metaphors.","""
📚 LESSON: Idioms & Figurative Language
📖 NOTES:
IDIOM — phrase where literal meaning ≠ real meaning:
  "It's raining cats and dogs." → raining very hard
  "Break a leg."               → Good luck!
  "Hit the books."             → Study hard
  "Let the cat out of the bag." → Reveal a secret

SIMILE — compare using 'like' or 'as':
  "She was as brave as a lion."

METAPHOR — one thing IS another:
  "Life is a roller coaster."

PERSONIFICATION — human quality to non-human:
  "The sun smiled down on us."

✏️ PRACTICE:
Q1. What does "hit the books" mean? ___
Q2. Simile or metaphor? "Her laughter was music to his ears." ___
Q3. Write your own simile about the sea: ___
Q4. Identify personification: "The angry storm tore through the town." ___

✅ ANSWERS: Q1=study hard  Q2=metaphor  Q4=angry storm
""")
        )
        else -> listOf(
            L("g7-e1","Comprehension Strategies","22 min",Subject.ENGLISH,7,false,"Use skimming, scanning and critical reading.","""
📚 LESSON: Comprehension Strategies
📖 NOTES:
SKIMMING  — read quickly for MAIN IDEA (headings, first sentences)
SCANNING  — read quickly to find SPECIFIC INFORMATION (dates, names)
CLOSE READING — read word by word for comprehension

CRITICAL READING:
  • What is the author's PURPOSE? (inform/persuade/entertain)
  • What is the TONE? (formal/angry/hopeful/sarcastic)
  • Is there BIAS or exaggeration?
  • What evidence supports the claims?

QUESTION LEVELS:
  Literal      → "What does the text say?"
  Inferential  → "What does the author imply?"
  Evaluative   → "Do you agree? Why or why not?"

✏️ PRACTICE:
Q1. Which strategy finds a specific date? ___
Q2. What strategy gives the gist of a text? ___
Q3. Write one inferential question about a text you read: ___
"""),
            L("g7-e2","Complex Sentences","20 min",Subject.ENGLISH,7,false,"Write compound and complex sentences.","""
📚 LESSON: Complex Sentences
📖 NOTES:
SIMPLE: one main clause — "The dog barked."
COMPOUND: two main clauses + FANBOYS (For And Nor But Or Yet So)
  "The dog barked, and the cat ran away."
COMPLEX: main clause + subordinate clause + subordinating conjunction
  although, because, when, if, unless, since, until, while

"Although it was raining, she went for a run."
(subordinate clause)       (main clause)

⚠️ A subordinate clause CANNOT stand alone.

✏️ PRACTICE:
Q1. Join with a conjunction: "He studied hard. He passed." ___
Q2. Subordinate clause in: "Unless you hurry, you will miss the bus." ___
Q3. Write a complex sentence using 'although': ___
Q4. Simple, compound or complex? "She sang while he played." ___

✅ ANSWERS: Q1=He studied hard, so he passed.  Q2=Unless you hurry  Q4=complex
"""),
            L("g7-e3","Transactional Writing","22 min",Subject.ENGLISH,7,false,"Write formal letters, emails and reports.","""
📚 LESSON: Transactional Writing
📖 NOTES:
FORMAL LETTER LAYOUT:
  [Your address — top right]
  [Date]
  [Recipient name & address — left]
  Dear Mr/Ms [Surname],
  [Introduction / Body / Conclusion]
  Yours sincerely,    ← when you KNOW the name
  Yours faithfully,   ← when you DON'T know the name
  [Your full name]

FORMAL EMAIL:
  To: / Subject: [clear and specific]
  Dear [Name],  [Formal body]  Kind regards,  [Name]

REPORT LAYOUT:
  Title / Introduction / Findings (with subheadings)
  Conclusion / Recommendation

✏️ WRITING TASK:
Write a formal letter to your school principal requesting
permission to start an environmental club. Include:
  ✅ Correct layout   ✅ Purpose of the club
  ✅ Benefits to school   ✅ Correct sign-off
""")
        )
    }
    private fun scienceFor(g: Int) = when (g) {
        1 -> listOf(
            L("g1-s1","Living & Non-living Things","12 min",Subject.NATURAL_SCIENCE,1,false,"Tell the difference between living and non-living things.","""
📚 LESSON: Living & Non-living Things
📖 NOTES:
LIVING THINGS — they are ALIVE. They:
✅ Grow  ✅ Breathe  ✅ Need food & water
✅ Move  ✅ Reproduce  ✅ Respond to surroundings
Examples: animals, plants, people, fungi

NON-LIVING THINGS — NOT alive:
❌ Don't grow  ❌ Don't breathe  ❌ Don't reproduce
Examples: rock, table, car, water, clouds

⚠️ TRICKY: Fire — not living (no cells, no reproduction)
           Seeds — living (they will grow)
           Wood  — was once living

✏️ PRACTICE:
Sort: dog / stone / flower / plastic bag / mushroom / river
Q1. Living: ___
Q2. Non-living: ___
Q3. Give TWO things every living thing needs: ___
Q4. Is a candle flame living? Explain: ___

✅ ANSWERS: Q1=dog,flower,mushroom  Q2=stone,plastic bag,river  Q4=No — doesn't reproduce
"""),
            L("g1-s2","My Body","12 min",Subject.NATURAL_SCIENCE,1,false,"Name basic body parts and their functions.","""
📚 LESSON: My Body
📖 NOTES:
BODY PARTS AND THEIR JOBS:
Eyes  👁  → See (sight)        Ears  👂 → Hear
Nose  👃 → Smell & breathe    Mouth 👄 → Eat, drink, speak
Hands ✋ → Touch & hold        Feet  🦶 → Walk & run
Heart ❤️ → Pumps blood         Lungs 🫁 → Breathe oxygen
Brain 🧠 → Controls everything

CARING FOR YOUR BODY:
🛁 Wash daily   🦷 Brush teeth twice daily
🥗 Eat healthy food   💤 Sleep 9–11 hours
🏃 Exercise regularly

✏️ PRACTICE:
Q1. Which organ pumps blood? ___
Q2. What do lungs do? ___
Q3. Match: ears→___   eyes→___   nose→___
Q4. Name TWO ways to care for your body: ___

✅ ANSWERS: Q1=heart  Q2=breathe in oxygen  Q3=hearing/sight/smell
"""),
            L("g1-s3","Weather Around Us","10 min",Subject.NATURAL_SCIENCE,1,false,"Observe and describe types of weather.","""
📚 LESSON: Weather Around Us
📖 NOTES:
Weather = what the air outside feels like each day.

TYPES OF WEATHER:
☀️ Sunny   🌧️ Rainy   💨 Windy
⛅ Cloudy  ⛈️ Stormy  ❄️ Snowy  🌫️ Foggy

WEATHER AFFECTS US:
Hot day   → wear light clothes, drink water
Rainy day → umbrella, raincoat
Cold day  → warm clothes, jersey

INSTRUMENTS:
Thermometer → measures temperature
Rain gauge  → measures rainfall
Wind vane   → shows wind direction

✏️ PRACTICE:
Q1. What type of weather has thunder and lightning? ___
Q2. What would you wear on a cold rainy day? ___
Q3. Which instrument measures temperature? ___

✅ ANSWERS: Q1=stormy  Q2=warm clothes and raincoat  Q3=thermometer
""")
        )
        2 -> listOf(
            L("g2-s1","Parts of a Plant","14 min",Subject.NATURAL_SCIENCE,2,false,"Name and describe the main parts of a plant.","""
📚 LESSON: Parts of a Plant
📖 NOTES:
THE 5 MAIN PARTS:

🌱 ROOTS — underground, absorb water & minerals, anchor plant
🌿 STEM  — holds plant upright, carries water & food
🍃 LEAVES — make food using sunlight (photosynthesis)
🌸 FLOWER — reproduction, attracts insects for pollination
🍎 FRUIT/SEED — contains seeds that grow into new plants

✏️ PRACTICE:
Q1. Which part absorbs water? ___
Q2. Where does the plant make its food? ___
Q3. What is the job of the flower? ___
Q4. Which part holds the plant upright? ___
Q5. Draw a plant and label its 5 parts.

✅ ANSWERS: Q1=roots  Q2=leaves  Q3=reproduction/making seeds  Q4=stem
"""),
            L("g2-s2","Animals & Their Habitats","12 min",Subject.NATURAL_SCIENCE,2,false,"Match animals to their natural habitats.","""
📚 LESSON: Animals & Their Habitats
📖 NOTES:
HABITAT = the natural HOME of an animal.

HABITATS:
Forest   → monkey, snake, parrot, deer
Ocean    → shark, whale, octopus, dolphin
Desert   → camel, scorpion, lizard
Grassland→ lion, zebra, giraffe, elephant
Wetland  → frog, hippo, crocodile, stork
Arctic   → polar bear, penguin, seal

ADAPTATIONS (special features for their habitat):
Camel    → humps store water (desert)
Fish     → gills breathe underwater
Polar bear → thick fur (cold Arctic)

✏️ PRACTICE:
Q1. Where does a shark live? ___
Q2. Which animal is adapted to the desert? ___
Q3. Match: frog→___   lion→___   penguin→___
Q4. Why does a fish have gills? ___

✅ ANSWERS: Q1=ocean  Q2=camel  Q3=wetland/grassland/arctic  Q4=to breathe underwater
"""),
            L("g2-s3","Materials Around Us","12 min",Subject.NATURAL_SCIENCE,2,false,"Compare properties of different materials.","""
📚 LESSON: Materials Around Us
📖 NOTES:
Materials = what objects are made from.

COMMON MATERIALS:
Wood    — hard, natural, can be cut, burns
Metal   — hard, shiny, conducts heat, can rust
Plastic — light, waterproof, can be moulded
Fabric  — soft, flexible, can absorb water
Glass   — transparent, smooth, breaks easily
Rubber  — flexible, waterproof, bouncy

PROPERTIES VOCABULARY:
Hard/Soft   Transparent/Opaque   Waterproof
Flexible/Rigid   Shiny/Dull   Natural/Man-made

✏️ PRACTICE:
Q1. Name a waterproof material: ___
Q2. Why is metal used to make pots? ___
Q3. Which material for a raincoat? ___
Q4. Is wood natural or man-made? ___

✅ ANSWERS: Q1=plastic/rubber  Q2=conducts heat  Q3=rubber/plastic  Q4=natural
""")
        )
        3 -> listOf(
            L("g3-s1","Life Cycles","16 min",Subject.NATURAL_SCIENCE,3,false,"Describe life cycles of butterfly, frog and plant.","""
📚 LESSON: Life Cycles
📖 NOTES:
A LIFE CYCLE shows how a living thing grows, reproduces and dies.

🦋 BUTTERFLY (4 stages — METAMORPHOSIS):
  Egg → Caterpillar (larva) → Pupa (chrysalis) → Adult butterfly

🐸 FROG (4 stages):
  Frogspawn (egg) → Tadpole → Froglet → Adult frog
  Tadpoles live in water; adults live on land.

🌱 PLANT:
  Seed → Germination → Seedling → Adult plant
  → Flower → Pollination → Fruit/Seed → new cycle

✏️ PRACTICE:
Q1. 4 stages of butterfly life cycle? ___
Q2. What is a young frog that lives in water? ___
Q3. What must happen for a seed to start growing? ___
Q4. Draw and label the butterfly life cycle.

✅ ANSWERS: Q1=egg,caterpillar,pupa,butterfly  Q2=tadpole  Q3=germination
"""),
            L("g3-s2","The Water Cycle","10 min",Subject.NATURAL_SCIENCE,3,false,"Explain evaporation, condensation and precipitation.","""
📚 LESSON: The Water Cycle
📖 NOTES:
The WATER CYCLE = continuous movement of water on Earth.

STAGES:
1️⃣ EVAPORATION — sun heats water → becomes water vapour (gas) → rises
2️⃣ CONDENSATION — water vapour cools → forms tiny droplets → clouds
3️⃣ PRECIPITATION — drops fall as rain, snow, sleet or hail
4️⃣ COLLECTION — water collects in oceans, rivers, soil → cycle repeats

KEY WORDS:
Evaporation  = liquid → gas (heat)
Condensation = gas → liquid (cooling)
Precipitation = water falling from clouds
Transpiration = water released by plants

✏️ PRACTICE:
Q1. What causes evaporation? ___
Q2. What are clouds made of? ___
Q3. Name TWO forms of precipitation: ___
Q4. Order: precipitation / condensation / evaporation / collection → ___

✅ ANSWERS: Q1=heat from sun  Q2=tiny water droplets  Q3=rain and snow
           Q4=evaporation→condensation→precipitation→collection
"""),
            L("g3-s3","Push & Pull Forces","12 min",Subject.NATURAL_SCIENCE,3,false,"Understand how forces affect movement.","""
📚 LESSON: Push & Pull Forces
📖 NOTES:
FORCE = a push or pull acting on an object.

PUSH — away from you: kicking a ball, pressing a button
PULL — towards you: pulling a drawer, opening a zip

WHAT FORCES DO:
✅ Start/stop movement  ✅ Speed up/slow down
✅ Change direction      ✅ Change shape

FRICTION — slows things down
  Rough surface = MORE friction
  Smooth surface = LESS friction

GRAVITY — pulls everything towards Earth

✏️ PRACTICE:
Q1. Is kicking a ball a push or pull? ___
Q2. Name TWO things a force can do: ___
Q3. What force pulls us to the ground? ___
Q4. Ball on rough gravel or smooth tiles — which is faster? ___

✅ ANSWERS: Q1=push  Q3=gravity  Q4=smooth tiles (less friction)
""")
        )
        4 -> listOf(
            L("g4-s1","Food Chains","12 min",Subject.NATURAL_SCIENCE,4,false,"Understand producers, consumers and decomposers.","""
📚 LESSON: Food Chains
📖 NOTES:
A FOOD CHAIN shows how energy passes through living things.
Always starts: SUN → PLANT

ROLES:
🌱 PRODUCER — makes own food (plants, algae)
🐛 PRIMARY CONSUMER — eats producer (herbivore): grasshopper, rabbit
🦅 SECONDARY CONSUMER — eats primary consumer: frog, fox
🦁 TERTIARY CONSUMER — eats secondary consumer: lion, eagle
🍄 DECOMPOSER — breaks down dead matter: fungi, bacteria

EXAMPLE: Sun → Grass → Grasshopper → Frog → Eagle

ARROWS show direction of ENERGY FLOW (→ = "is eaten by")

✏️ PRACTICE:
Q1. What is a producer? Give an example: ___
Q2. What do decomposers do? ___
Q3. Draw a food chain with 4 organisms: ___
Q4. In: Grass→Rabbit→Fox→Lion, what is the rabbit? ___

✅ ANSWERS: Q1=plant making own food e.g.grass  Q2=break down dead matter  Q4=primary consumer
"""),
            L("g4-s2","The Solar System","12 min",Subject.NATURAL_SCIENCE,4,false,"Name and describe the planets.","""
📚 LESSON: The Solar System
📖 NOTES:
THE 8 PLANETS in order from the Sun:
My Very Energetic Mother Just Served Us Nachos
Mercury Venus Earth Mars Jupiter Saturn Uranus Neptune

PLANET FACTS:
Mercury  — smallest, closest to Sun, no atmosphere
Venus    — hottest planet, thick clouds
Earth    — only planet with liquid water and life
Mars     — red planet, tallest volcano (Olympus Mons)
Jupiter  — largest, Great Red Spot (giant storm)
Saturn   — famous rings of ice and rock
Uranus   — tilted on its side, icy
Neptune  — farthest, strongest winds

THE MOON — Earth's natural satellite
Orbits Earth every 27 days, reflects Sun's light

✏️ PRACTICE:
Q1. Which planet is 3rd from the Sun? ___
Q2. Planet with famous rings? ___
Q3. Write all 8 planets in order: ___
Q4. What makes Earth special? ___

✅ ANSWERS: Q1=Earth  Q2=Saturn  Q4=liquid water and life
"""),
            L("g4-s3","States of Matter","11 min",Subject.NATURAL_SCIENCE,4,false,"Describe solids, liquids and gases.","""
📚 LESSON: States of Matter
📖 NOTES:
MATTER = anything with mass that takes up space.

SOLID   — definite shape & volume, particles tightly packed
           Examples: ice, rock, wood, metal

LIQUID  — definite volume, NO definite shape (takes shape of container)
           Particles flow past each other
           Examples: water, milk, oil, juice

GAS     — no definite shape or volume, fills any space
           Particles far apart, move fast
           Examples: air, steam, oxygen, CO₂

CHANGES OF STATE:
Solid→Liquid = MELTING (heat added)
Liquid→Solid = FREEZING (heat removed)
Liquid→Gas   = EVAPORATION (heat added)
Gas→Liquid   = CONDENSATION (heat removed)

✏️ PRACTICE:
Q1. What state is water when it becomes ice? ___
Q2. What is it called when liquid becomes gas? ___
Q3. Give TWO examples of gases: ___
Q4. Why does liquid take the shape of its container? ___

✅ ANSWERS: Q1=solid  Q2=evaporation  Q3=air and oxygen  Q4=particles can flow
""")
        )
        5 -> listOf(
            L("g5-s1","Energy Sources","12 min",Subject.NATURAL_SCIENCE,5,false,"Compare renewable and non-renewable energy.","""
📚 LESSON: Energy Sources
📖 NOTES:
NON-RENEWABLE — will run out:
  Coal, oil, natural gas, nuclear (uranium)
  ❌ Produces pollution and greenhouse gases (CO₂)
  ✅ Currently cheap and reliable

RENEWABLE — will never run out:
  Solar (Sun)  Wind  Hydropower (water)
  Biomass (organic)  Geothermal (Earth's heat)
  ✅ Clean, environmentally friendly
  ❌ Can be more expensive to set up

📌 South Africa generates most electricity from COAL.
   Solar and wind are growing rapidly.

✏️ PRACTICE:
Q1. Name TWO non-renewable energy sources: ___
Q2. What makes solar energy renewable? ___
Q3. ONE advantage of renewable energy: ___
Q4. Why is coal considered harmful? ___

✅ ANSWERS: Q1=coal,oil  Q2=sun won't run out  Q3=clean/no pollution  Q4=produces CO₂
"""),
            L("g5-s2","Human Body Systems","14 min",Subject.NATURAL_SCIENCE,5,false,"Describe the digestive, circulatory and respiratory systems.","""
📚 LESSON: Human Body Systems
📖 NOTES:
🍽️ DIGESTIVE SYSTEM:
  Mouth→Oesophagus→Stomach→Small intestine→Large intestine→Anus
  • Saliva starts digestion in mouth
  • Stomach uses acid to break down food
  • Small intestine absorbs nutrients into blood

❤️ CIRCULATORY SYSTEM:
  Heart + Blood vessels (arteries, veins, capillaries) + Blood
  • Arteries carry blood AWAY from heart (oxygenated)
  • Veins carry blood BACK to heart (deoxygenated)

🫁 RESPIRATORY SYSTEM:
  Nose/Mouth→Trachea→Bronchi→Lungs→Alveoli
  • Gas exchange happens in ALVEOLI
  • Breathe in O₂ → into blood
  • Breathe out CO₂ → out of blood

✏️ PRACTICE:
Q1. What organ pumps blood? ___
Q2. Where does gas exchange happen? ___
Q3. Which organ absorbs nutrients from food? ___
Q4. What do arteries carry? ___

✅ ANSWERS: Q1=heart  Q2=alveoli  Q3=small intestine  Q4=oxygenated blood
"""),
            L("g5-s3","Mixtures & Solutions","11 min",Subject.NATURAL_SCIENCE,5,false,"Distinguish mixtures from solutions.","""
📚 LESSON: Mixtures & Solutions
📖 NOTES:
MIXTURE — substances mixed but NOT chemically joined
  Can be separated. Each keeps own properties.
  Examples: salad, soil, sand & water, iron filings & salt

SOLUTION — solute DISSOLVES in solvent
  Solute + Solvent = Solution
  Salt + Water = Salt solution
  SOLUBLE = dissolves (sugar, salt)
  INSOLUBLE = does not dissolve (sand, chalk)

SEPARATION METHODS:
Filtration        — separates insoluble solids from liquid (sand from water)
Evaporation       — removes water, leaves solute (salt from salt water)
Magnetic separation — separates magnetic metals (iron from sand)
Sieving           — separates by size (stones from sand)

✏️ PRACTICE:
Q1. Is sugar dissolved in water a mixture or solution? ___
Q2. How to separate sand from water? ___
Q3. Method to separate iron from sand? ___
Q4. Is chalk soluble or insoluble? ___

✅ ANSWERS: Q1=solution  Q2=filtration  Q3=magnetic separation  Q4=insoluble
""")
        )
        6 -> listOf(
            L("g6-s1","Biodiversity","12 min",Subject.NATURAL_SCIENCE,6,false,"Classify living organisms and understand biodiversity.","""
📚 LESSON: Biodiversity
📖 NOTES:
BIODIVERSITY = the variety of all living things on Earth.

THE 5 KINGDOMS:
Animalia  — animals, multicellular, can't make own food
Plantae   — plants, make own food via photosynthesis
Fungi     — mushrooms, moulds, absorb nutrients
Protista  — single-celled (amoeba, algae)
Monera    — bacteria, simplest organisms

CLASSIFICATION hierarchy:
Kingdom→Phylum→Class→Order→Family→Genus→Species

WHY BIODIVERSITY MATTERS:
✅ Provides food, medicine and materials
✅ Clean air and water
✅ Each species plays a role
⚠️ THREATS: deforestation, pollution, climate change, poaching

📌 South Africa = 10% of all world plant species!

✏️ PRACTICE:
Q1. Which kingdom do mushrooms belong to? ___
Q2. Name TWO threats to biodiversity: ___
Q3. What do all Animalia have in common? ___

✅ ANSWERS: Q1=Fungi  Q2=deforestation and pollution  Q3=multicellular, can't make own food
"""),
            L("g6-s2","Electricity & Circuits","14 min",Subject.NATURAL_SCIENCE,6,false,"Understand simple electric circuits.","""
📚 LESSON: Electricity & Circuits
📖 NOTES:
ELECTRICITY = flow of electrons through a conductor.
CIRCUIT = a closed loop for electricity to flow.

COMPONENTS:
Battery/Cell — provides energy      Bulb — light
Switch — opens/closes circuit       Wire — carries electrons
Resistor — reduces current

CIRCUIT TYPES:
SERIES — all in ONE loop; if one bulb breaks, ALL go out
PARALLEL — separate branches; if one breaks, OTHERS stay on

CONDUCTORS (allow flow): metal, copper, water
INSULATORS (block flow): rubber, plastic, wood

Open circuit  = switch open = no electricity
Closed circuit = switch closed = electricity flows

✏️ PRACTICE:
Q1. What happens when you open a circuit? ___
Q2. Name TWO conductors: ___
Q3. Difference between series and parallel circuits? ___
Q4. Draw a simple circuit with battery, bulb and switch.

✅ ANSWERS: Q1=electricity stops  Q2=copper wire and water
"""),
            L("g6-s3","Ecosystems","11 min",Subject.NATURAL_SCIENCE,6,false,"Understand ecosystems and food webs.","""
📚 LESSON: Ecosystems
📖 NOTES:
ECOSYSTEM = all living things (biotic) in an area + their non-living environment (abiotic).

Biotic factors:  plants, animals, fungi, bacteria
Abiotic factors: sunlight, water, soil, temperature, air

FOOD WEB = network of interconnected food chains.
More realistic than a single food chain.
Shows ALL feeding relationships in an ecosystem.

INTERDEPENDENCE — all organisms depend on each other.
If one species disappears, the whole ecosystem is affected.

Example: Remove frogs → insects increase → plants destroyed
→ other animals lose food → ecosystem unbalanced.

ECOSYSTEM TYPES:
Terrestrial: forest, grassland, desert
Aquatic: freshwater (rivers), marine (ocean)

✏️ PRACTICE:
Q1. Name TWO abiotic factors: ___
Q2. How does a food web differ from a food chain? ___
Q3. What might happen if all grass disappeared from a grassland? ___

✅ ANSWERS: Q1=sunlight and water  Q2=food web shows multiple feeding relationships
""")
        )
        else -> listOf(
            L("g7-s1","Atoms & Molecules","14 min",Subject.NATURAL_SCIENCE,7,false,"Understand atomic structure.","""
📚 LESSON: Atoms & Molecules
📖 NOTES:
ATOM = smallest particle of an element.
Parts: Nucleus (protons+ and neutrons) + Electrons- in shells

ELEMENT = pure substance of ONE type of atom only.
  oxygen(O), carbon(C), iron(Fe), gold(Au)

MOLECULE = two or more atoms bonded together.
  H₂O = 2 hydrogen + 1 oxygen = WATER
  CO₂ = 1 carbon + 2 oxygen = CARBON DIOXIDE
  O₂  = 2 oxygen atoms = OXYGEN GAS

COMPOUND = two or more DIFFERENT elements bonded.
  H₂O is a compound.  O₂ is NOT (same element).

PERIODIC TABLE: organises all elements by atomic number.

✏️ PRACTICE:
Q1. What particles are in the nucleus? ___
Q2. Symbol for oxygen? ___
Q3. How many atoms in one H₂O molecule? ___
Q4. Is O₂ an element or compound? Explain: ___

✅ ANSWERS: Q1=protons and neutrons  Q2=O  Q3=3  Q4=element (same type of atom)
"""),
            L("g7-s2","Photosynthesis & Respiration","12 min",Subject.NATURAL_SCIENCE,7,false,"Explain how plants make food and cells release energy.","""
📚 LESSON: Photosynthesis & Respiration
📖 NOTES:
🌱 PHOTOSYNTHESIS (in leaves/chloroplasts):
  CO₂ + H₂O + sunlight → Glucose + O₂
  Needs: sunlight, chlorophyll, CO₂, water
  Produces: glucose (energy for plant) + oxygen

🔋 CELLULAR RESPIRATION (in all living cells/mitochondria):
  Glucose + O₂ → CO₂ + H₂O + ENERGY
  Aerobic (with O₂) — efficient
  Anaerobic (without O₂) — yeast making alcohol

COMPARISON:
                Photosynthesis    Respiration
Where:          Plant leaves      All living cells
Uses:           CO₂, H₂O, light  Glucose, O₂
Produces:       Glucose, O₂      CO₂, H₂O, energy
When:           Daytime only      Day and night

✏️ PRACTICE:
Q1. Gas released during photosynthesis? ___
Q2. Where in the cell does respiration occur? ___
Q3. Word equation for photosynthesis: ___
Q4. Can respiration happen in the dark? ___

✅ ANSWERS: Q1=oxygen  Q2=mitochondria  Q4=yes
"""),
            L("g7-s3","Forces & Motion","11 min",Subject.NATURAL_SCIENCE,7,false,"Describe Newton's Laws and forces.","""
📚 LESSON: Forces & Motion
📖 NOTES:
FORCE = push or pull, measured in Newtons (N)
SPEED     = distance ÷ time  (m/s or km/h)
VELOCITY  = speed + direction
ACCELERATION = change in speed ÷ time

NEWTON'S 3 LAWS:

1️⃣ LAW OF INERTIA
"An object at rest stays at rest; in motion stays in motion —
UNLESS a force acts on it."
Example: ball rolls, friction eventually stops it.

2️⃣ LAW OF ACCELERATION:  F = m × a
Bigger force = more acceleration; heavier object needs more force.
Example: pushing a heavy trolley vs light one.

3️⃣ ACTION & REACTION
"Every action has an equal and opposite reaction."
Example: Rocket pushes gas down → gas pushes rocket UP.

✏️ PRACTICE:
Q1. Car 1000kg, acceleration 2m/s² → Force = ___  (F=ma)
Q2. Which law explains why seat belts are needed? ___
Q3. Give an everyday example of Newton's 3rd Law: ___
Q4. Cyclist: 30km in 2 hours → speed = ___

✅ ANSWERS: Q1=2000N  Q2=1st Law (inertia)  Q4=15km/h
""")
        )
    }
    private fun lifeSkillsFor(g: Int) = when (g) {
        1 -> listOf(
            L("g1-l1","My Feelings","10 min",Subject.LIFE_SKILLS,1,false,"Recognise and name different emotions.","""
📚 LESSON: My Feelings
📖 NOTES:
Feelings (emotions) are normal. Everyone has them!

COMMON FEELINGS:
😊 Happy     — something good happens
😢 Sad       — something hurts or goes wrong
😠 Angry     — something is unfair
😨 Scared    — something feels dangerous
😲 Surprised — something unexpected
😐 Bored     — nothing interesting to do
🥰 Loved     — when we feel cared for

HOW TO DEAL WITH FEELINGS:
✅ Take 3 deep breaths to calm down
✅ Talk to a trusted adult
✅ Draw or write about how you feel
✅ Go outside and play

✏️ ACTIVITIES:
Q1. How do you feel on your birthday? ___
Q2. How do you feel when a friend takes your pencil? ___
Q3. Name a healthy way to express anger: ___
Q4. Draw 4 faces: happy, sad, angry, scared.
"""),
            L("g1-l2","Being Kind","9 min",Subject.LIFE_SKILLS,1,false,"Show kindness and care to others.","""
📚 LESSON: Being Kind
📖 NOTES:
KINDNESS = caring about others shown through words and actions.

ACTS OF KINDNESS at school:
✅ Share your snack  ✅ Help a struggling friend
✅ Say "please" and "thank you"
✅ Include someone who is left out
✅ Compliment someone: "You did really well!"

ACTS OF KINDNESS at home:
✅ Help set the table  ✅ Keep your room tidy
✅ Look after a younger sibling

THE GOLDEN RULE:
"Treat others the way you want to be treated."

💡 Kindness is CONTAGIOUS — when you are kind, others feel
inspired to be kind too!

✏️ ACTIVITIES:
Q1. Name ONE kind thing you did today: ___
Q2. How do you feel when someone is kind to you? ___
Q3. What would you do if someone was crying alone? ___
Q4. Write a kind message to give to a classmate: ___
"""),
            L("g1-l3","Personal Safety","11 min",Subject.LIFE_SKILLS,1,false,"Know about trusted adults and body safety.","""
📚 LESSON: Personal Safety
📖 NOTES:
PERSONAL SAFETY = keeping your body and feelings safe.

TRUSTED ADULTS:
👩 Parent or guardian   👨‍🏫 Teacher
👮 Police officer        👩‍⚕️ Doctor or nurse
🏠 A neighbour your family trusts

YOUR BODY BELONGS TO YOU:
✅ Only YOU decide who touches your body
✅ Private parts are covered by your swimsuit
✅ Nobody should ask to see/touch private parts
✅ It is NEVER your fault if someone makes you unsafe

THE 3 SAFETY RULES:
🔴 Say NO clearly and loudly
🏃 GET AWAY from the situation
📢 TELL a trusted adult immediately

ROAD SAFETY:
✅ Look LEFT, RIGHT, LEFT before crossing
✅ Cross at pedestrian crossings or traffic lights

✏️ ACTIVITIES:
Q1. Name TWO trusted adults in your life: ___
Q2. What should you do if you feel unsafe? ___
Q3. What are the 3 Safety Rules?  1.___ 2.___ 3.___
""")
        )
        2 -> listOf(
            L("g2-l1","Good Habits & Hygiene","12 min",Subject.LIFE_SKILLS,2,false,"Daily routines for good hygiene.","""
📚 LESSON: Good Habits & Hygiene
📖 NOTES:
HYGIENE = keeping your body clean to stay healthy.

DAILY ROUTINE:
🌅 Morning: brush teeth, wash face & hands, bath/shower, clean clothes, healthy breakfast
🌙 Evening: brush teeth, wash face & hands, 9–11 hours sleep

HAND WASHING — MOST IMPORTANT HABIT:
Wash hands BEFORE eating, AFTER toilet, after coughing,
after playing outside.
How: soap + water for 20 seconds, rinse, dry.

DENTAL HYGIENE:
• Brush twice a day — morning AND before bed
• Brush for at least 2 minutes
• Brush your tongue too!

✏️ ACTIVITIES:
Q1. How long should you wash your hands? ___
Q2. Name THREE times you must wash your hands: ___
Q3. How many times daily should you brush your teeth? ___
Q4. Why is sleep important? ___
"""),
            L("g2-l2","Making Friends","10 min",Subject.LIFE_SKILLS,2,false,"Skills for making and keeping friendships.","""
📚 LESSON: Making Friends
📖 NOTES:
A FRIEND = someone who cares about you and enjoys your company.

WHAT MAKES A GOOD FRIEND:
✅ Kind and caring  ✅ Honest  ✅ Good listener
✅ Loyal  ✅ Fun  ✅ Respectful

HOW TO MAKE NEW FRIENDS:
1. Smile and say hello
2. "Hi! My name is ___."
3. "Can I play with you?"
4. Listen when they speak
5. Be kind and share

HOW TO BE A GOOD FRIEND:
✅ Be honest  ✅ Keep promises  ✅ Apologise when wrong
✅ Stand up for your friend  ✅ Celebrate their success

WHAT FRIENDSHIP IS NOT:
❌ Bullying, teasing, leaving someone out

✏️ ACTIVITIES:
Q1. Name THREE qualities of a good friend: ___
Q2. How would you introduce yourself to someone new? ___
Q3. What should you do if you hurt your friend's feelings? ___
"""),
            L("g2-l3","Road Safety","11 min",Subject.LIFE_SKILLS,2,false,"Rules for staying safe near roads.","""
📚 LESSON: Road Safety
📖 NOTES:
PEDESTRIAN RULES:
✅ Use the pavement/sidewalk
✅ Look LEFT, RIGHT, LEFT before crossing
✅ Cross at traffic lights or pedestrian crossings
✅ Make eye contact with drivers
✅ Never run into the road
✅ Wear bright/reflective clothing at night

TRAFFIC SIGNS:
🔴 Red circle = STOP or PROHIBITION
🔵 Blue sign  = INSTRUCTION (obey)
⚠️ Triangle  = WARNING (danger ahead)

VEHICLE SAFETY:
✅ Always wear a seatbelt
✅ Keep head and hands inside the vehicle
✅ Never distract the driver

DANGERS: ❌ Playing in the road  ❌ Crossing between parked cars

✏️ ACTIVITIES:
Q1. What should you do before crossing the road? ___
Q2. What does a red traffic sign mean? ___
Q3. Name TWO road safety rules for pedestrians: ___
Q4. Why must you wear a seatbelt? ___
""")
        )
        3 -> listOf(
            L("g3-l1","Conflict Resolution","12 min",Subject.LIFE_SKILLS,3,false,"Resolve disagreements peacefully.","""
📚 LESSON: Conflict Resolution
📖 NOTES:
CONFLICT = a disagreement between people. Conflicts are normal
— it matters HOW you handle them.

WRONG WAYS: ❌ Fighting  ❌ Name-calling  ❌ Revenge

PEACEFUL STEPS:
1. CALM DOWN — breathe deeply, count to 10
2. LISTEN — let the other person speak
3. USE "I" STATEMENTS: "I feel ___ when you ___."
4. LOOK FOR A SOLUTION — brainstorm options
5. COMPROMISE — both people give a little
6. APOLOGISE if you were wrong
7. ASK AN ADULT if you cannot solve it

"I" STATEMENT EXAMPLES:
"I feel hurt when you leave me out of games."
"I feel angry when you take my things without asking."

SCENARIO: Sipho and Lerato both want the same book.
Q1. What should they do first? ___
Q2. Write an "I" statement Sipho could use: ___
Q3. Suggest a compromise: ___
Q4. When should they get a teacher? ___
"""),
            L("g3-l2","Healthy Eating","12 min",Subject.LIFE_SKILLS,3,false,"Learn about food groups and balanced diet.","""
📚 LESSON: Healthy Eating
📖 NOTES:
THE FOOD GROUPS:
Starchy Foods — bread, rice, maize, potato → ENERGY
Vegetables    — spinach, carrots, peas → vitamins & minerals
Fruit         — apples, bananas, oranges → vitamins, fibre
Proteins      — chicken, fish, eggs, beans → builds muscles
Dairy         — milk, yoghurt, cheese → strong bones (calcium)
Fats & Oils   — avocado, nuts, oil → small amounts only

THE PLATE MODEL:
Half plate  = VEGETABLES & FRUIT
Quarter     = STARCHY FOODS
Quarter     = PROTEIN
+ small dairy serving

LIMIT: sugar, salt, fried food, fizzy drinks, sweets
WATER: drink 6–8 glasses per day!

✏️ ACTIVITIES:
Q1. Which food group gives energy? ___
Q2. Name a food rich in protein: ___
Q3. How many glasses of water per day? ___
Q4. Plan a balanced lunch for yourself: ___
"""),
            L("g3-l3","Goal Setting","10 min",Subject.LIFE_SKILLS,3,false,"Set SMART goals and work towards them.","""
📚 LESSON: Goal Setting
📖 NOTES:
A GOAL = something you want to achieve in the future.

TYPES:
Short-term — days or weeks: "I will finish homework by Friday."
Long-term  — months or years: "I want to become a doctor."

SMART GOALS:
S — Specific   (clear and exact)
M — Measurable (you can track progress)
A — Achievable (possible to reach)
R — Relevant   (matters to you)
T — Time-bound (has a deadline)

WEAK: "I want to be better at maths."
SMART: "I will practise 5 maths problems every evening
        for 2 weeks to improve my multiplication."

STEPS TO ACHIEVE YOUR GOAL:
1. Write it down  2. Break into small steps
3. Work on it daily  4. Track progress  5. Celebrate! 🎉

✏️ ACTIVITIES:
Q1. Write a SMART goal for school: ___
Q2. Break it into 3 steps: Step 1:___ Step 2:___ Step 3:___
Q3. Is "I want to read more" a SMART goal? Why/Why not? ___
""")
        )
        4 -> listOf(
            L("g4-l1","Dealing with Peer Pressure","14 min",Subject.LIFE_SKILLS,4,false,"Recognise and resist peer pressure.","""
📚 LESSON: Dealing with Peer Pressure
📖 NOTES:
PEER PRESSURE = when people your age influence you to behave
in a certain way — sometimes against your values.

POSITIVE: "Come join the study group!"
NEGATIVE: "Everyone is doing it — don't be a baby."
          "If you don't, you're not our friend."

HOW TO SAY NO:
1. Say NO clearly: "No, I don't want to."
2. Give a reason: "That's against my values."
3. Suggest something else instead.
4. Walk away if needed.
5. Choose supportive friends.

THE 3 Rs:
Recognise — spot the pressure
Refuse    — say no assertively
Report    — tell a trusted adult if needed

SCENARIO: Friend dares you to skip school.
Q1. What type of peer pressure is this? ___
Q2. Write what you would say to refuse: ___
Q3. What might happen if you gave in? ___
Q4. Who could you talk to? ___
"""),
            L("g4-l2","Rights & Responsibilities","16 min",Subject.LIFE_SKILLS,4,false,"Understand children's rights and responsibilities.","""
📚 LESSON: Rights & Responsibilities
📖 NOTES:
RIGHT = something you are ENTITLED to as a human being.
RESPONSIBILITY = a DUTY you have towards others.

KEY CHILDREN'S RIGHTS (UNCRC / SA Constitution):
✅ Right to education
✅ Right to safety and protection from harm
✅ Right to healthcare
✅ Right to a name and identity
✅ Right to food, shelter and clothing
✅ Right to play and rest
✅ Right to language and culture

RIGHTS + RESPONSIBILITIES:
Right to education  → Attend school and do my best
Right to be safe    → Not bully or harm others
Right to be heard   → Listen respectfully to others
Right to play       → Play fairly, include everyone

✏️ ACTIVITIES:
Q1. Name TWO rights every child has: ___
Q2. If you have the right to education, what is your responsibility? ___
Q3. Which right is being violated when a bully stops someone from playing? ___
Q4. Name ONE responsibility you have at home: ___
"""),
            L("g4-l3","Study Skills","14 min",Subject.LIFE_SKILLS,4,false,"Organise study time effectively.","""
📚 LESSON: Study Skills
📖 NOTES:
STUDY ENVIRONMENT:
✅ Quiet space, good lighting  ✅ Desk and chair
✅ Phone on silent  ✅ All materials ready

STUDY METHODS THAT WORK:
📝 Mind Maps — central topic, branch out
🗂️ Flashcards — question one side, answer other
📖 Summarising — key points in your own words
🗣️ Teach someone — explain it out loud
✏️ Practice Questions — past papers!

THE POMODORO TECHNIQUE:
Study 25 min → rest 5 min → repeat 4× → break 30 min

TIMETABLE TIPS:
• Study HARDEST subject first
• Spread subjects across days
• Revise the day BEFORE a test, not the night before!

✏️ ACTIVITIES:
Q1. Create a study timetable for one week (5 subjects).
Q2. Name TWO effective study methods: ___
Q3. How long do you study before a Pomodoro break? ___
Q4. When is the BEST time to revise before a test? ___
""")
        )
        5 -> listOf(
            L("g5-l1","Growing Up & Puberty","16 min",Subject.LIFE_SKILLS,5,false,"Understand puberty changes.","""
📚 LESSON: Growing Up & Puberty
📖 NOTES:
PUBERTY = the time your body changes from child to adult. NORMAL!

WHEN: Girls 8–13 years   Boys 9–14 years

CHANGES IN GIRLS:
• Body grows taller  • Hips widen  • Breasts develop
• Body hair grows  • Menstruation begins

CHANGES IN BOYS:
• Grows taller, shoulders broaden  • Voice deepens
• Body and facial hair grows  • Muscles get bigger

CHANGES IN BOTH:
• Skin may get oily → acne (pimples)
• Increased sweating → use deodorant
• Emotional mood swings

HOW TO MANAGE:
✅ Wash daily with soap  ✅ Use deodorant
✅ Wear clean clothes daily  ✅ Talk to a trusted adult
✅ Remember: everyone goes through this!

✏️ REFLECTION:
Q1. At what age does puberty usually begin in girls? ___
Q2. Name TWO changes in BOTH boys and girls: ___
Q3. Why is daily washing especially important during puberty? ___
Q4. Who can you talk to if you have questions? ___
"""),
            L("g5-l2","Digital Safety","15 min",Subject.LIFE_SKILLS,5,false,"Stay safe online and protect your privacy.","""
📚 LESSON: Digital Safety
📖 NOTES:
DIGITAL SAFETY = protecting yourself when using the internet.

KEEP PRIVATE ONLINE:
❌ Full name, ID number  ❌ Home address, school name
❌ Phone number  ❌ Passwords  ❌ Photos to strangers

THE SMART RULES:
S — SAFE: keep personal info private
M — MEET: NEVER meet online friends alone in person
A — ACCEPT: don't accept files from unknown people
R — RELIABLE: not everything online is true
T — TELL: tell a trusted adult if something upsets you

CYBERBULLYING = bullying online (mean messages, excluding someone)
If it happens to you:
✅ DON'T respond  ✅ Block the person
✅ Screenshot as evidence  ✅ Tell a trusted adult

SCREEN TIME: max 2 hours recreational per day for children.

✏️ ACTIVITIES:
Q1. THREE things NEVER to share online: ___
Q2. What to do if cyberbullied? ___
Q3. Is everything you read online true? Explain: ___
Q4. What does 'T' in SMART rules stand for? ___
"""),
            L("g5-l3","Critical Thinking","14 min",Subject.LIFE_SKILLS,5,false,"Evaluate information and make good decisions.","""
📚 LESSON: Critical Thinking
📖 NOTES:
CRITICAL THINKING = thinking carefully and logically before you
believe or do something.

QUESTIONS A CRITICAL THINKER ASKS:
❓ Is this information true? What is the source?
❓ Is there another way to look at this?
❓ What evidence supports this?
❓ Am I influenced by emotions or facts?
❓ What might happen if I believe/do this?

FAKE NEWS — false information spread online:
✅ Check the source  ✅ Read more than one source
✅ Check the date   ✅ Does it seem too shocking?

DECISION MAKING — PRO/CON LIST:
Write PROS (advantages) and CONS (disadvantages) of each option.

Example: Spend pocket money OR save it?
Spend: PRO=enjoyment now  CON=no savings
Save:  PRO=more later     CON=wait for reward

✏️ ACTIVITIES:
Q1. "Chocolate cures all diseases." How would a critical thinker respond? ___
Q2. Pro/con list: studying more vs watching TV ___
Q3. Name ONE question to ask before sharing news online: ___
""")
        )
        6 -> listOf(
            L("g6-l1","Substance Abuse Awareness","18 min",Subject.LIFE_SKILLS,6,false,"Understand dangers of drugs and alcohol.","""
📚 LESSON: Substance Abuse Awareness
📖 NOTES:
SUBSTANCE ABUSE = using drugs, alcohol or other substances in a harmful way.

SUBSTANCES AND RISKS:
Alcohol      — damages liver, impairs judgement, addictive
Tobacco/Vape — cancer, lung disease, addiction
Cannabis     — brain development damage in teens, memory loss
Tik/Meth     — severe brain damage, heart failure, addiction
Solvents     — brain damage, suffocation risk

STAGES OF ADDICTION:
Experiment → Regular use → Dependence → Addiction

WHY TEENS ARE AT HIGHER RISK:
The teenage brain is still developing (judgement & self-control).
Substances cause MORE damage to developing brains.

REFUSAL SKILLS:
✅ "No thanks, I'm not interested."
✅ "That would ruin my sport performance."
✅ Walk away from the situation
✅ Choose friends who share your values

HELP: SANCA: 011 892 3829   Lifeline: 0861 322 322

✏️ ACTIVITIES:
Q1. Name TWO harmful effects of alcohol: ___
Q2. Why are young people especially at risk? ___
Q3. Write a refusal statement for a cigarette offer: ___
Q4. What is the first stage of addiction? ___
"""),
            L("g6-l2","Leadership Skills","15 min",Subject.LIFE_SKILLS,6,false,"Develop qualities of a good leader.","""
📚 LESSON: Leadership Skills
📖 NOTES:
A LEADER = someone who guides, inspires and supports others.

QUALITIES OF A GOOD LEADER:
✅ Honest and trustworthy  ✅ Good communicator
✅ Responsible (owns decisions)  ✅ Empathetic
✅ Organised and focused  ✅ Leads by EXAMPLE

LEADERSHIP STYLES:
Democratic  — involves others in decisions (collaborative)
Autocratic  — leader decides alone (good in a crisis)
Laissez-faire — gives team freedom (good for creative tasks)

TEAMWORK SKILLS:
✅ Communicate openly  ✅ Listen to all ideas
✅ Divide tasks fairly  ✅ Support struggling members
✅ Celebrate success together

SOUTH AFRICAN LEADERS:
Nelson Mandela — forgiveness, perseverance, integrity

✏️ ACTIVITIES:
Q1. Name THREE qualities of a good leader: ___
Q2. Difference between democratic and autocratic leadership? ___
Q3. Describe a time you showed leadership: ___
Q4. How can you be a better team player? ___
"""),
            L("g6-l3","Career Awareness","16 min",Subject.LIFE_SKILLS,6,false,"Explore different career paths.","""
📚 LESSON: Career Awareness
📖 NOTES:
A CAREER = the type of work you do over your lifetime.

CAREER CLUSTERS:
Science & Tech   — Doctor, Engineer, Pharmacist, Data Scientist
Arts             — Graphic Designer, Musician, Architect, Writer
Business         — Accountant, Entrepreneur, Lawyer, Manager
Education        — Teacher, Lecturer, Counsellor
Health & Caring  — Nurse, Social Worker, Paramedic
Trades           — Electrician, Plumber, Mechanic, IT Technician
Agriculture      — Farmer, Veterinarian, Food Scientist

WHAT DETERMINES A CAREER?
• Your INTERESTS  • Your STRENGTHS  • Your VALUES
• Education and QUALIFICATIONS required

EDUCATION PATHWAYS after Grade 12:
University    → Degree (3–6 years)
TVET College  → Diploma/Certificate (1–3 years)
Apprenticeship → Trade qualification (2–4 years)

✏️ ACTIVITIES:
Q1. Name TWO careers you are interested in: ___
Q2. What subjects are important to become a doctor? ___
Q3. Do you need a university degree to be successful? Explain: ___
Q4. List THREE of your strengths that could help in a career: ___
""")
        )
        else -> listOf(
            L("g7-l1","Healthy Relationships","18 min",Subject.LIFE_SKILLS,7,false,"Recognise healthy and unhealthy relationships.","""
📚 LESSON: Healthy Relationships
📖 NOTES:
HEALTHY RELATIONSHIP FEATURES:
✅ Mutual respect  ✅ Trust and honesty
✅ Open communication  ✅ Support and encouragement
✅ Independence (each keeps own identity)
✅ Equality — no one person has all the power

UNHEALTHY WARNING SIGNS:
❌ Jealousy and control  ❌ Disrespect or put-downs
❌ Isolation from friends/family
❌ Pressure to do things you don't want
❌ Fear of the other person's reactions
❌ Any form of physical, emotional or sexual abuse

YOUR RIGHTS IN ANY RELATIONSHIP:
✅ Right to say NO at any time  ✅ Right to feel safe
✅ Right to have your own opinions  ✅ Right to end a relationship

IF IN AN UNHEALTHY RELATIONSHIP:
1. Talk to a trusted adult
2. Set clear boundaries
3. Seek help — SADAG: 0800 456 789

✏️ REFLECTION:
Q1. Name FOUR features of a healthy relationship: ___
Q2. Name TWO warning signs of an unhealthy relationship: ___
Q3. Is checking your partner's phone healthy? Why? ___
Q4. What should you do if you feel unsafe? ___
"""),
            L("g7-l2","Financial Literacy Basics","16 min",Subject.LIFE_SKILLS,7,false,"Understand earning, saving, spending and budgeting.","""
📚 LESSON: Financial Literacy Basics
📖 NOTES:
NEEDS = things you must have to survive:
  food, water, shelter, clothing, healthcare

WANTS = things you'd like but don't need:
  new phone, designer shoes, snacks, gaming

INCOME   = money you earn or receive (pocket money, part-time job)
EXPENSES = money you spend
  Fixed: school fees, transport (same each month)
  Variable: food, clothing (changes each month)

BUDGET = plan for your money:
  Income − Expenses = Savings

BUDGET EXAMPLE (R200 pocket money):
  Income:      R200
  Transport:  −R80
  Snacks:     −R50
  Entertainment: −R30
  SAVINGS:     R40  ← always save something!

THE 50/30/20 RULE:
50% Needs  |  30% Wants  |  20% Savings

INTEREST: Save at bank → earn interest (extra money!)
           Borrow → pay interest (cost of borrowing)

✏️ ACTIVITIES:
Q1. Is a new pair of sneakers a need or a want? ___
Q2. Create a budget for R150 pocket money: ___
Q3. Difference between saving and borrowing? ___
Q4. 50/30/20 on R400 income → savings = ___

✅ ANSWERS: Q1=want  Q4=R80
"""),
            L("g7-l3","Entrepreneurship Intro","18 min",Subject.LIFE_SKILLS,7,false,"Understand basic entrepreneurship.","""
📚 LESSON: Entrepreneurship Introduction
📖 NOTES:
ENTREPRENEUR = a person who creates a business by identifying
a need and providing a solution.

QUALITIES:
✅ Creative  ✅ Risk-taker  ✅ Resilient
✅ Problem-solver  ✅ Disciplined  ✅ Passionate

THE BUSINESS IDEA PROCESS:
1. IDENTIFY a PROBLEM in your community
   "No affordable stationery near school."
2. BRAINSTORM SOLUTIONS
   "Sell second-hand stationery."
3. RESEARCH the market
   "Who would buy it? What would they pay?"
4. CREATE a simple plan — THE 4 Ps:
   Product → Price → Place → Promotion
5. START SMALL, learn and grow

YOUTH ENTREPRENEURSHIP EXAMPLES IN SA:
• Selling homemade food at school
• Tutoring younger learners
• Making and selling crafts/jewellery
• Washing cars in the neighbourhood

✏️ MINI PROJECT:
Identify ONE problem in your school or community.
Design a simple business to solve it.

Problem identified: ___
My business idea:   ___
Who will buy it?:   ___
What will I charge?: ___
How will I advertise?: ___
""")
        )
    }
}
