JFAA	38	FFSC	2010	40	4	7/1/2001	9/24/2010	12/31/2001	(21)94892207	Jonathan Farias Alves			
JFAA	PPETR62	38	2010	1	COD	Fábrica ETL Petrobrás	8	8	8	8	8	0	0
JFAA	0	38	2010	2	0	0	0	0	0	0	0	0	0
JFAA	0	38	2010	3	0	0	0	0	0	0	0	0	0
JFAA	0	38	2010	4	0	0	0	0	0	0	0	0	0
JFAA	0	38	2010	5	0	0	0	0	0	0	0	0	0
JFAA	0	38	2010	6	0	0	0	0	0	0	0	0	0
JFAA	0	38	2010	7	0	0	0	0	0	0	0	0	0
JFAA	0	38	2010	8	0	0	0	0	0	0	0	0	0
JFAA	0	38	2010	9	0	0	0	0	0	0	0	0	0
JFAA	0	38	2010	10	0	0	0	0	0	0	0	0	0
JFAA	0	38	2010	11	0	0	0	0	0	0	0	0	0
JFAA	0	38	2010	12	0	0	0	0	0	0	0	0	0
JFAA	0	38	2010	13	0	0	0	0	0	0	0	0	0
JFAA	0	38	2010	14	0	0	0	0	0	0	0	0	0
JFAA	0	38	2010	15	0	0	0	0	0	0	0	0	0
JFAA	0	38	2010	16	0	0	0	0	0	0	0	0	0
JFAA	0	38	2010	17	0	0	0	0	0	0	0	0	0
JFAA	0	38	2010	18	0	0	0	0	0	0	0	0	0
JFAA	0	38	2010	19	0	0	0	0	0	0	0	0	0
JFAA	0	38	2010	20	0	0	0	0	0	0	0	0	0
JFAA	0	38	2010	21	0	0	0	0	0	0	0	0	0
JFAA	0	38	2010	22	0	0	0	0	0	0	0	0	0
JFAA	0	38	2010	23	0	0	0	0	0	0	0	0	0
JFAA	0	38	2010	24	0	0	0	0	0	0	0	0	0
JFAA	0	38	2010	25	0	0	0	0s('opacity', 0).css(ref, motion == 'pos' ? -distance : distance); // Shift

		// Animation
		var animation = {opacity: mode == 'show' ? 1 : 0};
		animation[ref] = (mode == 'show' ? (motion == 'pos' ? '+=' : '-=') : (motion == 'pos' ? '-=' : '+=')) + distance;

		// Animate
		el.animate(animation, { queue: false, duration: o.duration, easing: o.options.easing, complete: function() {
			if(mode == 'hide') el.hide(); // Hide
			$.effects.restore(el, props); $.effects.removeWrapper(el); // Restore
			if(o.callback) o.callback.apply(this, arguments); // Callback
			el.dequeue();
		}});

	});

};

})(jQuery);
