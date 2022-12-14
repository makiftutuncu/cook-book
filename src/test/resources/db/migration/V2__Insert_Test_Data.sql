INSERT INTO ingredients(name, vegetarian)
VALUES ('Egg', true),
       ('Sugar', true),
       ('Flour', true),
       ('Milk', true),
       ('Oil', true),
       ('Baking Powder', true),
       ('Vanilla', true),
       ('Salt', true),
       ('Honey', true);

INSERT INTO recipes(name, number_of_servings)
VALUES ('Pancakes', 4);

INSERT INTO recipe_ingredients(recipe_id, ingredient_id, label, value, unit, sort_order)
VALUES (1, 1, 'For Batter', 2, 'QUANTITY', 1),
       (1, 2, 'For Batter', 0.5, 'CUPS', 2),
       (1, 3, 'For Batter', 2, 'CUPS', 3),
       (1, 4, 'For Batter', 1.5, 'CUPS', 4),
       (1, 5, 'For Batter', 2, 'SPOONS', 5),
       (1, 6, 'For Batter', 1, 'PACKS', 6),
       (1, 7, 'For Batter', 1, 'PACKS', 7),
       (1, 8, 'For Batter', 1, 'PINCHES', 8),
       (1, 5, 'For Cooking', 2, 'SPOONS', 1),
       (1, 9, 'For Serving', 3, 'SPOONS', 1);

INSERT INTO recipe_instructions(recipe_id, sort_order, instruction)
VALUES (1, 1, 'Break eggs and add sugar.'),
       (1, 2, 'Mix until creamy.'),
       (1, 3, 'Add oil and milk and continue mixing.'),
       (1, 4, 'Add flour, baking powder, vanilla and salt.'),
       (1, 5, 'Mix until smooth.'),
       (1, 6, 'Slightly oil a pan and heat. Reduce heat once pan is hot.'),
       (1, 7, 'Pour mixture into pan.'),
       (1, 8, 'Cook until bubbles appear on top and flip to cook the other side.'),
       (1, 9, 'Cook all of the batter the same way.'),
       (1, 10, 'Pour honey on top and serve.');
